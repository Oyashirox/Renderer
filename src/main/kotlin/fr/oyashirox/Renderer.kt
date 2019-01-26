package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Point
import fr.oyashirox.model.Model
import kotlin.random.Random

/** Use this class to render a model on a canvas*/
class Renderer(val canvas: Canvas) {
    fun renderWireframeModel(model: Model) {
        val halfWidth = (canvas.image.width - 1) / 2.0f
        val halfHeight = (canvas.image.height - 1) / 2.0f
        model.faces.forEach { face ->
            // Convert world to screen coord (orthographic)
            val points = face.vertices
                .map {
                    val x = ((it.x + 1) * halfWidth).toInt()
                    val y = ((it.y + 1) * halfHeight).toInt()
                    Point(
                        x, y
                    )
                }
            // Create the triangle
            points.forEachIndexed { index, point ->
                val point2 = points[(index + 1) % 3]
                canvas.line(point, point2, Color.WHITE)
            }
        }
    }

    fun renderFlat(model: Model) {
        val halfWidth = (canvas.image.width - 1) / 2.0f
        val halfHeight = (canvas.image.height - 1) / 2.0f
        val random = Random(0)
        model.faces.forEach { face ->
            // Convert world to screen coord (orthographic)
            val points = face.vertices
                .map {
                    val x = ((it.x + 1) * halfWidth).toInt()
                    val y = ((it.y + 1) * halfHeight).toInt()
                    Point(
                        x, y
                    )
                }
            // Create the triangle
            canvas.triangle(points, Color(random.nextDouble(), random.nextDouble(), random.nextDouble()))
        }
    }
}