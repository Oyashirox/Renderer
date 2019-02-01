package fr.oyashirox.gl

import fr.oyashirox.math.*
import kotlin.math.abs

@Suppress("MemberVisibilityCanBePrivate")
class GL(val image: Image) {
    private val zBuffer = DoubleArray(image.width * image.height).apply { fill(-Double.MAX_VALUE) }

    fun triangle(triangle: List<Point>, toInterpolate: List<List<Vector>>, shader: Shader) {
        val min = Point(triangle.minBy { it.x }!!.x, triangle.minBy { it.y }!!.y)
        val max = Point(triangle.maxBy { it.x }!!.x, triangle.maxBy { it.y }!!.y)

        for (x in min.x.coerceAtLeast(0)..max.x.coerceAtMost(image.width - 1)) {
            for (y in min.y.coerceAtLeast(0)..max.y.coerceAtMost(image.height - 1)) {
                val index = x + y * image.width
                val barycenter = barycentric(triangle, Point(x, y))
                if (barycenter.x < 0 || barycenter.y < 0 || barycenter.z < 0) continue

                // TODO fix the z interpolation
                // See: https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/visibility-problem-depth-buffer-depth-interpolation
                val zValue = interpolate(barycenter, triangle) { zBuffer }
                if(zValue < 0 || zBuffer[index] >= zValue) continue

                val interpolated = toInterpolate.map { interpolate(barycenter, it) }
                shader.interpolate = interpolated.toMutableList()

                val color = shader.fragment(this, barycenter) ?: continue
                zBuffer[index] = zValue
                image[x, y] = color
            }
        }
    }

    fun debugDepth(): Image {
        val argbArray = zBuffer.map { Color(it.toInt(), it.toInt(), it.toInt()).argbColor }.toIntArray()
        return Image(this.image.width, this.image.height, argbArray)
    }

    private fun barycentric(triangle: List<Point>, point: Point): Vector {
        val (p1, p2, p3) = triangle
        //x is along AC, y is along AB and z is along AP
        val xVector = Vector((p3.x - p1.x).toDouble(), (p2.x - p1.x).toDouble(), (p1.x - point.x).toDouble())
        val yVector = Vector((p3.y - p1.y).toDouble(), (p2.y - p1.y).toDouble(), (p1.y - point.y).toDouble())
        val u = xVector.cross(yVector)

        // I'm not completely sure about that but here we
        // Reorganize so that x is a factor of A, y is a factor of B, and z is a factor of C
        // Also, dividing by Z because homogeneous coords
        return if (abs(u.z) > 1e-2)
            Vector(1.0 - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z)
        else
            Vector(-1.0, 1.0, 1.0)
    }

    private fun <T> interpolate(weights: Vector, values: List<T>, value: T.() -> Double) =
        weights.x * values[0].value() +
                weights.y * values[1].value() +
                weights.z * values[2].value()

    private fun interpolate(weights: Vector, values: List<Vector>): Vector =
        weights.x * values[0] +
                weights.y * values[1] +
                weights.z * values[2]
}