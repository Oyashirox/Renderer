package fr.oyashirox

import fr.oyashirox.math.Image
import fr.oyashirox.model.Model
import java.awt.Desktop
import java.nio.file.Paths
import kotlin.system.measureTimeMillis


fun main() {
    val image = Image(800, 800)
    val canvas = Canvas(image)
    val renderer = Renderer(canvas)

    val time = measureTimeMillis {
        val africanHeadFile = Paths.get(".", "obj", "african_head.obj").toAbsolutePath().normalize().toFile()
        val model = Model.fromObjFile(africanHeadFile)
        renderer.renderWireframeModel(model)

        image.flipVertically()
    }

    println("time: ${time / 1000.0} s")
    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}