package fr.oyashirox.math

import kotlin.math.abs

@Suppress("NOTHING_TO_INLINE")
data class Point(var x: Int = 0, var y: Int = 0) {
    inline fun horizontalDistanceTo(o: Point) = abs(o.x - x)
    inline fun verticalDistanceTo(o: Point) = abs(o.y - y)
    inline fun swap() {
        val tmp = x
        x = y
        y= tmp
    }
}