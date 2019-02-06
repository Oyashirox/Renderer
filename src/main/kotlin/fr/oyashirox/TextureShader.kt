package fr.oyashirox

import fr.oyashirox.gl.GL
import fr.oyashirox.gl.Shader
import fr.oyashirox.math.*
import fr.oyashirox.model.Texture
import fr.oyashirox.model.VertexAttributes

@Suppress("MemberVisibilityCanBePrivate")
class TextureShader(val lightDir: Vector, val mvp: Matrix, val texture: Texture, val normalTex: Texture) : Shader {
    override var interpolate: MutableList<Vector> = mutableListOf()

    override fun vertex(context: GL, attributes: VertexAttributes): Point {
        interpolate.add(attributes.texCoords)
        interpolate.add(attributes.normal)
        interpolate.add(attributes.tangent)
        return (mvp * attributes.vertex.toMatrix()).toVector().toPoint()
    }

    override fun fragment(context: GL, weight: Vector): Color? {
        val texCoords = interpolate[0]
        val normal = computeNormal()
        val intensity = normal.dot(-lightDir).coerceAtLeast(0.0)
        return intensity * texture[texCoords]
    }

    private fun computeNormal(): Vector {
        val texCoords = interpolate[0]
        val normal = interpolate[1].normalize()
        val normalizedTangent = interpolate[2].normalize()

        // Renormalize
        val tangent = (normalizedTangent - (normalizedTangent.dot(normal) * normal)).normalize()
        val bitangent = tangent.cross(normal)

        val texNormal = normalTex[texCoords].toVector() * 2.0 - 1.0
        // This should be a 3x3, but our Matrix class only handles 4x4 for now
        val tbn = Matrix(4, true)
        tbn[0] = tangent
        tbn[1] = bitangent
        tbn[2] = normal
        tbn.transpose()

        return (tbn * texNormal.toMatrix()).toVector().normalize()
    }
}