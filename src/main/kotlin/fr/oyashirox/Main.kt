package fr.oyashirox

import fr.oyashirox.gl.Camera
import fr.oyashirox.gl.GL
import fr.oyashirox.gl.Renderer
import fr.oyashirox.gl.Shader
import fr.oyashirox.math.Image
import fr.oyashirox.math.Vector
import fr.oyashirox.model.Model
import fr.oyashirox.model.Texture
import java.awt.Desktop
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

@Suppress("unused")
fun debugDepth(gl: GL) {
    val image = gl.debugDepth()
    image.flipVertically()
    val path = image.saveToDisk(Paths.get(".", "depthBuffer.png").toFile())
    Desktop.getDesktop().open(path)
}

enum class Models(val prefix: String) {
    DIABLO("diablo3"), AFRICAN("african_head");

}
fun loadModels(model: Models): Triple<Model, Texture, Texture> {
    val objFolder = Paths.get(".", "obj").normalize()
    val modelFile = objFolder.resolve(model.prefix + ".obj").toFile()
    val textureFile = objFolder.resolve(model.prefix + "_diffuse.png").toFile()
    val normalFile = objFolder.resolve(model.prefix + "_nm_tangent.png").toFile()

    val obj = Model.fromObjFile(modelFile)
    val texture = Texture.loadFromFile(textureFile)
    val textureNormal = Texture.loadFromFile(normalFile)

    return Triple(obj, texture, textureNormal)
}

fun main() {
    val image = Image(800, 800)
    val gl = GL(image)
    val camera = Camera(image.width, image.height)
    val renderer = Renderer(gl)


    val (model, texture, textureNormal) = loadModels(Models.DIABLO)
//    val grid = Texture.loadFromFile(Paths.get(".", "obj", "grid.png").toFile())
//    val defaultNormal = Texture.loadFromFile(Paths.get(".", "obj", "default_normal_tangent.png").toFile())

    val time = measureTimeMillis {
        camera.lookAt(Vector(1.0, 1.0, 4.0), Vector())
        val shader: Shader = TextureShader(Vector(-1.0, -1.0, 0.0).normalize(), camera.combinedMatrix, texture, textureNormal)
        renderer.render(model, shader)
        image.flipVertically()
    }
    println("time: ${time / 1000.0} s")

    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}