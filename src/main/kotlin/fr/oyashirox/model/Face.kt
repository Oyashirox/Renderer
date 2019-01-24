package fr.oyashirox.model

import fr.oyashirox.math.Vector

data class Face(val vertices: List<Vector>) {
    companion object {
        fun fromIndexes(vertices: List<Vector>, indexes: List<Int>) = Face(
            indexes.map { vertices[it] }
        )
    }
}