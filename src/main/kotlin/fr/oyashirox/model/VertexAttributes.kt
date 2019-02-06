package fr.oyashirox.model

import fr.oyashirox.math.Vector

data class VertexAttributes(
    val vertex: Vector,
    val texCoords: Vector,
    val normal: Vector,
    val tangent: Vector)