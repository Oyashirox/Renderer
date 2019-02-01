@file:Suppress("unused")

package fr.oyashirox.gl

import fr.oyashirox.math.Point
import fr.oyashirox.math.Vector
import fr.oyashirox.model.Model

/** Use this class to render a model on a canvas*/
class Renderer(val gl: GL) {
    fun render(model: Model, shader: Shader) {

        model.faces.forEach { face ->
            val fragments = mutableListOf<Point>()
            val interpolate = mutableListOf<MutableList<Vector>>()

            // Call to the first vertex. The shader then set the corresponding values that need to be interpolated
            fragments.add(shader.vertex(gl, face[0]))
            // For each interpolatable, we precreate a List of 3 Vector (1 for each vertex of the face)
            shader.interpolate.forEach { vector ->
                interpolate.add(MutableList(3) { vector })
            }
            // Clear the shader so next call can add all its value to be interpolated again
            shader.interpolate.clear()

            // Do the same thing for the other 2 vertices
            for(i in 1..2) {
                fragments.add(shader.vertex(gl, face[i]))
                shader.interpolate.forEachIndexed { index, vector ->
                    interpolate[index][i] = vector
                }
                shader.interpolate.clear()
            }


            gl.triangle(fragments, interpolate, shader)
            shader.interpolate.clear()
        }
    }
}