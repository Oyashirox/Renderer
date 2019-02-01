package fr.oyashirox.model

import fr.oyashirox.math.Vector

data class Face(
    val vertices: List<Vector>,
    val textures: List<Vector>,
    val normals: List<Vector>
) {
    companion object {
        fun fromIndexes(
            allVertices: List<Vector>,
            allTextures: List<Vector>,
            allNormals: List<Vector>,
            indexes: List<Triple<Int, Int, Int>>
        ): Face {
            val vertices = mutableListOf<Vector>()
            val textures = mutableListOf<Vector>()
            val normals = mutableListOf<Vector>()

            indexes.forEach {
                vertices.add(allVertices[it.first])
                textures.add(allTextures[it.second])
                normals.add(allNormals[it.third])
            }

            return Face(vertices, textures, normals)
        }
    }

    val attributes: Collection<VertexAttributes> = (0 until vertices.size).map { this[it] }
    operator fun get(i: Int) = VertexAttributes(vertices[i], textures[i], normals[i])
}