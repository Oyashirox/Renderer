package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import java.awt.Desktop

fun main(args: Array<String>) {
    val image = Image(100, 100)
    image[52, 41] = Color(255, 0, 0)
    image.flipVertically()
    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}