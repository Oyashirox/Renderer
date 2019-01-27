package fr.oyashirox

import fr.oyashirox.math.Image
import fr.oyashirox.math.Vector
import fr.oyashirox.model.Model
import fr.oyashirox.model.Texture
import java.awt.Desktop
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

@Suppress("unused")
fun debugDepth(canvas: Canvas) {
    val image = canvas.debugDepth()
    image.flipVertically()
    val path = image.saveToDisk(Paths.get(".", "depthBuffer.png").toFile())
    Desktop.getDesktop().open(path)
}

fun main() {
    val image = Image(800, 800)
    val canvas = Canvas(image)
    val camera = Camera(Vector(-1.0, -0.5, 3.0), image.width, image.height)
    val renderer = Renderer(canvas, camera)

    val objFolder = Paths.get(".", "obj").normalize()
    val africanHeadFile = objFolder.resolve("diablo.obj").toFile()
    val africanHeadTexture = objFolder.resolve("diablo3_pose_diffuse.png").toFile()
    val model = Model.fromObjFile(africanHeadFile)
    val texture = Texture.loadFromFile(africanHeadTexture)

    val time = measureTimeMillis {
        renderer.camera.lookAt(Vector(0.0, 0.0, 0.0))
        renderer.render(model, texture, Vector(0.0, 0.0, -1.0).normalize())
        image.flipVertically()
    }
    println("time: ${time / 1000.0} s")

    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
    debugDepth(canvas)
}