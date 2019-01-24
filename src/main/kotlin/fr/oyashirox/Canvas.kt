package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import fr.oyashirox.math.Point

/** Use this class to draw stuff on an image (like [line])*/
class Canvas(val image: Image) {
    fun line(start: Point, end: Point, color: Color) {
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

}