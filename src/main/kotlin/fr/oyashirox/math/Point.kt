package fr.oyashirox.math

import kotlin.math.abs

@Suppress("NOTHING_TO_INLINE")
data class Point(var x: Int = 0, var y: Int = 0, var zBuffer: Double = Double.MIN_VALUE) {
    inline operator fun plus(v: Point) = Point(x + v.x, y + v.y)
    inline operator fun minus(v: Point) = Point(x - v.x, y - v.y)
    inline fun horizontalDistanceTo(o: Point) = abs(o.x - x)
    inline fun verticalDistanceTo(o: Point) = abs(o.y - y)
    inline fun swap() {
        val tmp = x
        x = y
        y= tmp
    }
}