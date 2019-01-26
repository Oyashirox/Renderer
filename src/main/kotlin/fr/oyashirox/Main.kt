package fr.oyashirox

import fr.oyashirox.math.Image
import fr.oyashirox.model.Model
import fr.oyashirox.model.Texture
import java.awt.Desktop
import java.nio.file.Paths


fun main() {
    val image = Image(800, 800)
    val canvas = Canvas(image)
    val renderer = Renderer(canvas)

    val objFolder = Paths.get(".", "obj").normalize()
    val africanHeadFile = objFolder.resolve("african_head.obj").toFile()
    val africanHeadTexture = objFolder.resolve("african_head_diffuse.png").toFile()
    val model = Model.fromObjFile(africanHeadFile)
    val texture = Texture.loadFromFile(africanHeadTexture)

//    val time = measureTimeMillis {
//        renderer.render(model, Vector(0.0, 0.0, -1.0).normalize())
//        image.flipVertically()
//    }
//    println("time: ${time / 1000.0} s")

    val path = texture.image.saveToDisk()
    Desktop.getDesktop().open(path)
}