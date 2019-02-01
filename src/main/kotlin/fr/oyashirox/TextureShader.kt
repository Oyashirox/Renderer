package fr.oyashirox

import fr.oyashirox.gl.GL
import fr.oyashirox.gl.Shader
import fr.oyashirox.math.*
import fr.oyashirox.model.Texture
import fr.oyashirox.model.VertexAttributes

@Suppress("MemberVisibilityCanBePrivate")
class TextureShader(val lightDir: Vector, val mvp: Matrix, val texture: Texture, val normalTex: Texture): Shader {
    override var interpolate: MutableList<Vector> = mutableListOf()

    override fun vertex(context: GL, attributes: VertexAttributes): Point {
        interpolate.add(attributes.texCoords)
        return (mvp * attributes.vertex.toMatrix()).toVector().toPoint()
    }

    override fun fragment(context: GL, weight: Vector): Color? {
        val texCoords = interpolate[0]
        val normal = (2.0 * normalTex[texCoords].toVector() - 1.0).normalize()
        val intensity = normal.dot(-lightDir).coerceAtLeast(0.0)
        return intensity * texture[texCoords]
    }
}