package fr.oyashirox

import fr.oyashirox.gl.GL
import fr.oyashirox.gl.Shader
import fr.oyashirox.math.*
import fr.oyashirox.model.Texture
import fr.oyashirox.model.VertexAttributes

@Suppress("MemberVisibilityCanBePrivate")
class TextureShader(val lightDir: Vector, val mvp: Matrix, val texture: Texture): Shader {
    override var interpolate: MutableList<Vector> = mutableListOf()

    override fun vertex(context: GL, attributes: VertexAttributes): Point {
        interpolate.add(attributes.texCoords)
        val intensity = attributes.normal.dot(-lightDir).coerceAtLeast(0.0)
        interpolate.add(Vector(intensity))
        return (mvp * attributes.vertex.toMatrix()).toVector().toPoint()
    }

    override fun fragment(context: GL, weight: Vector): Color? {
        val texCoords = interpolate[0]
        val intensity = interpolate[1].x
        return intensity * texture[texCoords]
    }
}