package fr.oyashirox

import fr.oyashirox.gl.GL
import fr.oyashirox.gl.Shader
import fr.oyashirox.math.*
import fr.oyashirox.model.VertexAttributes

@Suppress("MemberVisibilityCanBePrivate")
class GouraudShader(val lightDir: Vector, val mvp: Matrix): Shader {
    override var interpolate: MutableList<Vector> = mutableListOf()

    override fun vertex(context: GL, attributes: VertexAttributes): Point {
        val intensity = attributes.normal.dot(-lightDir).coerceAtLeast(0.0)
        interpolate.add(Vector(intensity))
        if(intensity > 1) {
            println("Test")
        }
        return (mvp * attributes.vertex.toMatrix()).toVector().toPoint()
    }

    override fun fragment(context: GL, weight: Vector): Color? {
        val intensity = interpolate[0].x

        return intensity * Color.WHITE
    }
}