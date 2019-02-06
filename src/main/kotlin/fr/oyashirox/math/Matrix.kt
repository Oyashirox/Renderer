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
    operator fun set(row: Int, v: Vector) {
        values[row * size] = v.x
        values[row * size + 1] = v.y
        values[row * size + 2] = v.z
        values[row * size + 3] = 0.0
    }

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

    fun transpose() {
        for (x in 0 until size) {
            for (y in x + 1 until size) {
                val tmp = this[x, y]
                this[x, y] = this[y, x]
                this[y, x] = tmp
            }
        }
    }

    fun toVector() = Vector(
        this[0,0] / this[3, 0],
        this[1,0] / this[3, 0],
        this[2,0] / this[3, 0]
    )
}