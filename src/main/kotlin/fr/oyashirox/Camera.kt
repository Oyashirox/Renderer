package fr.oyashirox

import fr.oyashirox.math.Matrix
import fr.oyashirox.math.Vector

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class Camera(val position: Vector, val width: Int, val height: Int) {
    val halfWidth = (width - 1) / 2.0
    val halfHeight = (height - 1) / 2.0
    val depth = 255.0

    private lateinit var z: Vector
    private lateinit var x: Vector
    private lateinit var y: Vector

    /** Transform world coordinates to camera coordinates */
    private lateinit var modelViewMatrix: Matrix

    /** Transform camera coordinates to clip coordinates */
    private lateinit var perspectiveMatrix: Matrix

    /** Transform clip coordinates to screen coordinates */
    private val viewportMatrix = Matrix(4, true).apply {
        // diagonal
        // Scale [-1,1] to [-halfWidth,halfWidth], same for height and to [-127.5,127.5] for depth
        this[0, 0] = halfWidth
        this[1, 1] = halfHeight
        this[2, 2] = depth / 2

        // rencenter so we get into [0, width], or [0, 255]
        this[0, 3] = halfWidth
        this[1, 3] = halfHeight
        this[2, 3] = depth / 2
    }

    val combinedMatrix: Matrix
        get() = viewportMatrix * perspectiveMatrix * modelViewMatrix

    init {
        lookAt(Vector(0.0, 0.0, 0.0))
    }

    fun lookAt(target: Vector, up: Vector = Vector(0.0, 1.0, 0.0)) {
        val bases = Matrix(4)
        val translation = Matrix(4)

        z = (position - target).normalize()
        x = up.cross(z).normalize()
        y = z.cross(x).normalize()

        bases[0] = x
        bases[1] = y
        bases[2] = z

        translation[0, 3] = -target.x
        translation[1, 3] = -target.y
        translation[2, 3] = -target.z

        perspectiveMatrix = Matrix(4, true).apply {
            this[3, 2] = -1.0 / (position - target).length()
        }

        modelViewMatrix = bases * translation
    }
}