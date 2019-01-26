@file:Suppress("NOTHING_TO_INLINE")

package fr.oyashirox.math

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Color(val r: UByte, val g: UByte, val b: UByte, val a: UByte = 0xFF.toUByte()) {
    constructor(r: Int = 0, g: Int = 0, b: Int = 0, a: Int = 255) : this(r.toUByte(), g.toUByte(), b.toUByte(), a.toUByte())
    constructor(r: Double, g: Double, b: Double, a: Double = 1.0) : this(
        (r * 255).toInt().toUByte(),
        (g * 255).toInt().toUByte(),
        (b * 255).toInt().toUByte(),
        (a * 255).toInt().toUByte()
    )

    var argbColor: Int = ((a shl 24) and 0xFF000000.toInt()) or
            ((r shl 16) and 0x00FF0000) or
            ((g shl 8) and 0x0000FF00) or
            (b.toInt() and 0x000000FF)

    fun toVector() = Vector((r.toInt() / 255.0), (g.toInt() / 255.0), (b.toInt() / 255.0))

    fun gamma2() = toVector().sqrt().toColor()

    private inline infix fun UByte.shl(shift: Int): Int = this.toInt() shl shift

    companion object {
        val RED = Color(255)
        val GREEN = Color(g = 255)
        val BLUE = Color(b = 255)
        val WHITE = Color(255, 255, 255)
    }
}

inline operator fun Double.times(v: Color) = Color(
    this * (v.r.toInt() / 255.0),
    this * (v.g.toInt() / 255.0),
    this * (v.b.toInt() / 255.0)
)

inline operator fun Vector.times(v: Color) = Color(
    this.x.toInt() * (v.r.toInt() / 255.0),
    this.y.toInt() * (v.g.toInt() / 255.0),
    this.z.toInt() * (v.b.toInt() / 255.0)
)

