package fr.oyashirox.model

import fr.oyashirox.math.Vector
import java.io.File

data class Model(val vertices: List<Vector>, val faces: List<List<Int>>) {
    companion object {
        fun fromObjFile(file: File): Model {
            val reader = file.bufferedReader()
            val vertices = mutableListOf<Vector>()
            val faces = mutableListOf<List<Int>>()
            reader.forEachLine { line ->
                when {
                    line.startsWith("v ") -> {
                        val values = line.drop(2)
                            .split(" ")
                            .map { it.toDouble() }
                        vertices.add(Vector(values[0], values[1], values[2]))
                    }

                    line.startsWith("f ") -> {
                        val values = line.drop(2)
                            .split(" ")
                            .map { point ->
                                // Take the first int of the triplet
                                point.split("/")
                                    .take(1)
                                    .map { it.toInt() }
                            }
                            .flatten()
                            .map { it - 1 } // Minus 1 because index starts at 1 in obj
                        faces.add(values)
                    }
                }
            }
            return Model(vertices, faces)
        }
    }
}

