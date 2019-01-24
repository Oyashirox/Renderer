@file:Suppress("NOTHING_TO_INLINE")

package fr.oyashirox.math

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Color(val r: Byte, val g: Byte, val b: Byte, val a: Byte = 0xFF.toByte()) {
    constructor(r: Int = 0, g: Int = 0, b: Int = 0, a: Int = 255) : this(r.toByte(), g.toByte(), b.toByte(), a.toByte())
    constructor(r: Double, g: Double, b: Double, a: Double = 1.0) : this(
        (r * 255).toByte(),
        (g * 255).toByte(),
        (b * 255).toByte(),
        (a * 255).toByte()
    )

    var argbColor: Int = (a shl 24) or (r shl 16) or (g shl 8) or b.toInt()

    inline operator fun plus(o: Color) = Color(r + o.r, g + o.g, b + o.b)

    fun toVector() = Vector((r / 255.0), (g / 255.0), (b / 255.0))

    fun gamma2() = toVector().sqrt().toColor()

    private inline infix fun Byte.shl(shift: Int): Int = this.toInt() shl shift

    companion object {
        val RED = Color(255)
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
    this.x * (v.r / 255.0),
    this.y * (v.g / 255.0),
    this.z * (v.b / 255.0)
)

