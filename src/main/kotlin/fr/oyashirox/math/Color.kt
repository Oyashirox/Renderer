@file:Suppress("NOTHING_TO_INLINE")

package fr.oyashirox.math

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Color(val argb: Int) {
    constructor(r: Int = 0, g: Int = 0, b: Int = 0, a: Int = 255) : this(
        ((a shl 24) and 0xFF000000.toInt()) or
                ((r shl 16) and 0x00FF0000) or
                ((g shl 8) and 0x0000FF00) or
                (b and 0x000000FF)
    )

    constructor(r: Double, g: Double, b: Double, a: Double = 1.0) : this(
        (r * 255).toInt(),
        (g * 255).toInt(),
        (b * 255).toInt(),
        (a * 255).toInt()
    )

    inline val r: Int
        get() = (argb and 0x00FF0000) shr 16
    inline val g: Int
        get() = (argb and 0x0000FF00) shr 8
    inline val b: Int
        get() = argb and 0x000000FF
    inline val a: Int
        get() = argb and 0xFF000000.toInt() shr 24


    companion object {
        val RED = Color(r = 255)
        val GREEN = Color(g = 255)
        val BLUE = Color(b = 255)
        val WHITE = Color(255, 255, 255)
    }
}

inline operator fun Double.times(v: Color) = Color(
    this * (v.r / 255.0),
    this * (v.g / 255.0),
    this * (v.b / 255.0)
)

inline operator fun Vector.times(v: Color) = Color(
    this.x.toInt() * (v.r / 255.0),
    this.y.toInt() * (v.g / 255.0),
    this.z.toInt() * (v.b / 255.0)
)

