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

fun main() {
    val image = Image(800, 800)
    val gl = GL(image)
    val camera = Camera(image.width, image.height)
    val renderer = Renderer(gl)

    val objFolder = Paths.get(".", "obj").normalize()
    val africanHeadFile = objFolder.resolve("diablo.obj").toFile()
    val africanHeadTexture = objFolder.resolve("diablo3_pose_diffuse.png").toFile()
    val model = Model.fromObjFile(africanHeadFile)
    val texture = Texture.loadFromFile(africanHeadTexture)

    val time = measureTimeMillis {
        camera.lookAt(Vector(1.0, 1.0, 4.0), Vector())
        val shader: Shader = TextureShader(Vector(-1.0, -1.0, 0.0).normalize(), camera.combinedMatrix, texture)
        renderer.render(model, shader)
        image.flipVertically()
    }
    println("time: ${time / 1000.0} s")

    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}