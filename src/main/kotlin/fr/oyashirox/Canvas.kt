package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import fr.oyashirox.math.Point
import fr.oyashirox.math.Vector

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

    fun triangleLineSweeping(points: List<Point>, color: Color) {
        val (p1, p2, p3) = points.sortedBy { it.y }
        if (p1.y == p2.y && p1.y == p3.y) return
        val longSide = p3 - p1 // Long side is between the lowest and highest point
        val shortSide1 = p2 - p1 // First part of the short side is between the lowest and middle point
        val shortSide2 = p3 - p2 // First part of the short side is between the middle and highest point
        if (shortSide1.y > 0)
            triangleLineSweepingSubPart(p1.y..p2.y, p1, p1, longSide, shortSide1, color)
        if (shortSide2.y > 0)
            triangleLineSweepingSubPart(p2.y..p3.y, p1, p2, longSide, shortSide2, color)
    }

    @Suppress("NOTHING_TO_INLINE")
    /**
     *  @param yRange range of y axis we are currently sweeping
     *  @param lowestPoint lowest point (on y axis) of the triangle
     *  @param startingPoint current starting point of the subpart (its y-coord must be equal to the lowest of yRange)
     *  @param longSide unit vector for the longest side
     *  @param shortSide unit vector for the current short side
     *
     * */
    private inline fun triangleLineSweepingSubPart(
        yRange: IntRange,
        lowestPoint: Point,
        startingPoint: Point,
        longSide: Point,
        shortSide: Point,
        color: Color
    ) {
        for (y in yRange) {
            val i =
                (y - yRange.start) / shortSide.y.toFloat() // normalize current position in [0, 1] along the first short side
            val j =
                (y - lowestPoint.y) / longSide.y.toFloat()  // normalize current position in [0, 1] along the long side
            val xShortSide = (startingPoint.x + shortSide.x * i).toInt()
            val xLongSide = (lowestPoint.x + longSide.x * j).toInt()

            val range = IntProgression.fromClosedRange(xShortSide, xLongSide, if (xShortSide < xLongSide) 1 else -1)
            for (x in range) {
                image[x, y] = color
            }
        }
    }

    fun triangle(triangle: List<Point>, color: Color) {
        val min = Point(triangle.minBy { it.x }!!.x, triangle.minBy { it.y }!!.y)
        val max = Point(triangle.maxBy { it.x }!!.x, triangle.maxBy { it.y }!!.y)

        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                val barycenter = barycentric(triangle, Point(x, y))
                if (barycenter.x < 0 || barycenter.y < 0 || barycenter.z < 0) continue
                image[x, y] = color
            }
        }
    }

    private fun barycentric(triangle: List<Point>, point: Point): Vector {
        val (p1, p2, p3) = triangle
        val xVector = Vector((p3.x - p1.x).toDouble(), (p2.x - p1.x).toDouble(), (p1.x - point.x).toDouble())
        val yVector = Vector((p3.y - p1.y).toDouble(), (p2.y - p1.y).toDouble(), (p1.y - point.y).toDouble())
        val u = xVector.cross(yVector)

        return Vector(1.0f - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z)
    }
}