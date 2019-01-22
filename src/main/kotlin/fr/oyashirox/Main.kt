package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import fr.oyashirox.math.Point
import java.awt.Desktop
import kotlin.system.measureTimeMillis

val red = Color(255)
val white = Color(255, 255, 255)

fun line(p1: Point, p2: Point, image: Image, color: Color) {
    // We always want to iterate on the longest axis, so if it is the vertical,
    // we swap x and y so iterating on x means iterating on y
    val swapped = if (p1.horizontalDistanceTo(p2) < p1.verticalDistanceTo(p2)) {
        p1.swap()
        p2.swap()
        true
    } else {
        false
    }
    val step = if (p1.x < p2.x) 1 else -1 // Handle both direction on x
    val range = IntProgression.fromClosedRange(p1.x, p2.x, step)
    val dx = p1.horizontalDistanceTo(p2)
    val dy = p1.verticalDistanceTo(p2)
    val m = dy * 2 // Slope of the line
    var error = 0.0
    var y = p1.y // Starts at p1, then increase of m after each step
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

fun main(args: Array<String>) {
    val image = Image(100, 100)

    val time = measureTimeMillis {
        line(p1 = Point(13, 20), p2 = Point(80, 40), image = image, color = white)
        line(p1 = Point(20, 13), p2 = Point(40, 80), image = image, color = red)
        line(p1 = Point(80, 40), p2 = Point(13, 20), image = image, color = red)
        // Flip vertically allows us to have the (0,0) at the bottom-left instead of up-left
        image.flipVertically()
    }

    println("time: ${time / 1000.0} s")
    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}