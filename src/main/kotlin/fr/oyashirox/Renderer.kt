@file:Suppress("unused")

package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Point
import fr.oyashirox.math.Vector
import fr.oyashirox.model.Model
import fr.oyashirox.model.Texture
import kotlin.random.Random

/** Use this class to render a model on a canvas*/
@Suppress("MemberVisibilityCanBePrivate")
class Renderer(val canvas: Canvas, val camera: Camera) {

    private fun mapToScreen(world: Vector): Point {
        val vector = (camera.combinedMatrix * world.toMatrix()).toVector()
        return vector.toPoint()
    }

    fun renderWireframeModel(model: Model) {
        model.faces.forEach { face ->
            // Convert world to screen coord (orthographic)
            val points = face.vertices.map(::mapToScreen)
            // Create the triangle
            points.forEachIndexed { index, point ->
                val point2 = points[(index + 1) % 3]
                canvas.line(point, point2, Color.WHITE)
            }
        }
    }

    fun renderFlat(model: Model) {
        val random = Random(0)
        model.faces.forEach { face ->
            // Convert world to screen coord (orthographic)
            val points = face.vertices.map(::mapToScreen)
            // Create the triangle
            canvas.triangle(points, Color(random.nextDouble(), random.nextDouble(), random.nextDouble()))
        }
    }

    fun render(model: Model, texture: Texture, lightDir: Vector) {
        model.faces.forEach { face ->
            // Convert world to screen coord (orthographic)
            val points = face.vertices.map(::mapToScreen)
            canvas.triangle(points, face, texture, lightDir)
        }
    }

}