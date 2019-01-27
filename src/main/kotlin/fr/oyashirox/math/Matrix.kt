package fr.oyashirox.math

@Suppress("MemberVisibilityCanBePrivate")
/** Square matrix only for now*/
class Matrix(val size: Int = 4, identity: Boolean = true) {
    val values = DoubleArray(size * size) { if (identity && it % size == it / size) 1.0 else 0.0 }
    operator fun get(row: Int, col: Int) = values[col + row * size]
    operator fun set(row: Int, col: Int, value: Double) {
        values[col + row * size] = value
    }

    operator fun get(row: Int) = values.slice((row * size)..(((row + 1) * size) - 1))

    operator fun times(other: Matrix): Matrix {
        if (size != other.size) throw IllegalStateException("Matrix should be of the same size")
        val result = Matrix(size)
        for (x in 0 until size) {
            for (y in 0 until size) {
                result[x, y] = 0.0
                for (z in 0 until size) {
                    result[x, y] += this[x, z] * other[z, y]
                }
            }
        }
        return result
    }

    fun toVector() = Vector(
        this[0,0] / this[3, 0],
        this[1,0] / this[3, 0],
        this[2,0] / this[3, 0]
    )
}