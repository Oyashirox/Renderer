package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import fr.oyashirox.math.Point
import fr.oyashirox.model.Model
import java.awt.Desktop
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

val red = Color(255)
val white = Color(255, 255, 255)

fun line(start: Point, end: Point, image: Image, color: Color) {
    val p1 = start.copy()
    val p2 = end.copy()
    // We always want to iterate on the longest axis, so if it is the vertical,
    // we swap x and y so iterating on x means iterating on y
    val swapped = if (p1.horizontalDistanceTo(p2) < p1.verticalDistanceTo(p2)) {
        p1.swap()
        p2.swap()
        true
    } else {
        false
    }
    // Handle both direction on x
    val range = IntProgression.fromClosedRange(p1.x, p2.x, if (p1.x < p2.x) 1 else -1)
    val dx = p1.horizontalDistanceTo(p2)
    val dy = p1.verticalDistanceTo(p2)
    val m = dy * 2 // Slope of the line
    var error = 0.0
    var y = p1.y // Starts at p1, then increase of m after each step
    val step = if (p1.y < p2.y) 1 else -1

    for (x in range) {
        if (swapped) {
            image[y, x] = color // Transpose back the swapped coordinates
        } else {
            image[x, y] = color
        }
        error += m
        if (error > dx) {
            y += step
            error -= dx * 2
        }
    }
}

fun renderWireframeModel(model: Model, image: Image) {
    val halfWidth = (image.width - 1) / 2.0f
    val halfHeight = (image.height - 1) / 2.0f
    model.faces.forEach { face ->
        val points = face
            .map { model.vertices[it] }
            .map {
                val x = ((it.x + 1) * halfWidth).toInt()
                val y = ((it.y + 1) * halfHeight).toInt()
                Point(
                    x,
                    y
                )
            }
        points.forEachIndexed { index, point ->
            try {
                val point2 = points[(index + 1) % 3]
                line(point, point2, image, white)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }
}

fun main(args: Array<String>) {
    val image = Image(800, 800)

    val time = measureTimeMillis {
        val africanHeadFile = Paths.get(".", "obj", "african_head.obj").toAbsolutePath().normalize().toFile()
        val model = Model.fromObjFile(africanHeadFile)
        renderWireframeModel(model, image)

        image.flipVertically()
    }

    println("time: ${time / 1000.0} s")
    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}