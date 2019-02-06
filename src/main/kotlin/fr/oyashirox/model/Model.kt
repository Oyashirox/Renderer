package fr.oyashirox.model

import fr.oyashirox.math.Vector
import java.io.File

private typealias TripleIndexes = Triple<Int, Int, Int>

@Suppress("MemberVisibilityCanBePrivate")
data class Model(
    val vertices: List<Vector>,
    val textureCoords: List<Vector>,
    val normals: List<Vector>,
    private val facesIndexes: List<List<TripleIndexes>>
) {
    val tangents = computeTangents(vertices, textureCoords, facesIndexes)
    val faces: List<Face> = facesIndexes.map { Face.fromIndexes(vertices, textureCoords, normals, tangents, it) }

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

        private fun computeTangents(
            vertices: List<Vector>,
            textureCoords: List<Vector>,
            facesIndexes: List<List<TripleIndexes>>
        ): List<Vector> {
            // Algorithm from https://learnopengl.com/Advanced-Lighting/Normal-Mapping
            // And http://ogldev.atspace.co.uk/www/tutorial26/tutorial26.html
            val tangents = vertices.map { Vector() }.toMutableList()
            facesIndexes.forEach { face ->
                val v1 = vertices[face[0].first]
                val v2 = vertices[face[1].first]
                val v3 = vertices[face[2].first]
                val uv1 = textureCoords[face[0].second]
                val uv2 = textureCoords[face[1].second]
                val uv3 = textureCoords[face[2].second]

                val edge1 = v2 - v1
                val edge2 = v3 - v1
                val deltaUV1 = uv2 - uv1
                val deltaUV2 = uv3 - uv1
                val f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y)

                val tangent = Vector()
                tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x)
                tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y)
                tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z)

                val tangentNormalized = tangent.normalize()
                tangents[face[0].first] += tangentNormalized
                tangents[face[1].first] += tangentNormalized
                tangents[face[2].first] += tangentNormalized
            }
            return tangents.map { it.normalize() }
        }
    }
}

