package fr.oyashirox.model

import fr.oyashirox.math.Vector
import java.io.File

private typealias TripleIndexes = Triple<Int, Int, Int>

data class Model(
    val vertices: List<Vector>,
    val textureCoords: List<Vector>,
    val normals: List<Vector>,
    private val facesIndexes: List<List<TripleIndexes>>
) {
    val faces: List<Face> = facesIndexes.map { Face.fromIndexes(vertices, textureCoords, normals, it) }

    companion object {
        fun fromObjFile(file: File): Model {
            val reader = file.bufferedReader()
            val vertices = mutableListOf<Vector>()
            val textureCoords = mutableListOf<Vector>()
            val normals = mutableListOf<Vector>()

            val faceIndexes = mutableListOf<List<TripleIndexes>>()
            reader.forEachLine { line ->
                when {
                    line.startsWith("v ") -> {
                        val values = line.drop(2)
                            .split(" ")
                            .map { it.toDouble() }
                        vertices.add(Vector(values[0], values[1], values[2]))
                    }
                    line.startsWith("vt ") -> {
                        val values = line.drop(3)
                            .split(" ")
                            .filter { !it.isEmpty() }
                            .map { it.toDouble() }
                        textureCoords.add(Vector(values[0], values[1], values[2]))
                    }
                    line.startsWith("vn ") -> {
                        val values = line.drop(3)
                            .split(" ")
                            .filter { !it.isEmpty() }
                            .map { it.toDouble() }
                        normals.add(Vector(values[0], values[1], values[2]))
                    }
                    line.startsWith("f ") -> {
                        val values = line.drop(2)
                            .split(" ")
                            .map { point ->
                                val indexes = point.split("/")
                                    .map { it.toInt() - 1 }
                                TripleIndexes(indexes[0], indexes[1], indexes[2])
                            }
                        faceIndexes.add(values)
                    }
                }
            }
            return Model(vertices, textureCoords, normals, faceIndexes)
        }
    }
}

