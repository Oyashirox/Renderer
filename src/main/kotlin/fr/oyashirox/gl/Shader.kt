package fr.oyashirox.gl

import fr.oyashirox.math.Color
import fr.oyashirox.math.Point
import fr.oyashirox.math.Vector
import fr.oyashirox.model.VertexAttributes

interface Shader {
    /** List of all values that needs to be interpolated between vertices.
     *
     * Right now it's only Vector, but we should also be able to interpolate Double.*/
    var interpolate: MutableList<Vector>
    fun vertex(context: GL, attributes: VertexAttributes): Point
    fun fragment(context: GL, weight: Vector): Color?
}