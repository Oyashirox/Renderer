package fr.oyashirox

import fr.oyashirox.math.Color
import fr.oyashirox.math.Image
import fr.oyashirox.math.Point
import java.awt.Desktop
import kotlin.system.measureTimeMillis


fun main() {
    val image = Image(200, 200)
    val canvas = Canvas(image)

    val time = measureTimeMillis {
        val t1 = listOf(Point(10, 70), Point(50, 160), Point(70, 80))
        val t2 = listOf(Point(180, 50), Point(150, 1), Point(70, 180))
        val t3 = listOf(Point(180, 150), Point(120, 160), Point(130, 180))

        canvas.triangle(t1, Color.RED)
        canvas.triangle(t2, Color.GREEN)
        canvas.triangle(t3, Color.BLUE)

        image.flipVertically()
    }

    println("time: ${time / 1000.0} s")
    val path = image.saveToDisk()
    Desktop.getDesktop().open(path)
}