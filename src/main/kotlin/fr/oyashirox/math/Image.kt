package fr.oyashirox.math

import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

@Suppress("NOTHING_TO_INLINE", "MemberVisibilityCanBePrivate")
/** Represents an Image. You can [set pixel colors][set] and [save it on the disk][saveToDisk]. */
class Image(val width: Int, val height: Int, argbArray: IntArray? = null) {
    val data = if(argbArray == null) {
        Array(width * height) { Color() }
    } else {
        Array(width * height) { Color(argb = argbArray[it]) }
    }

    inline operator fun get(x: Int, y: Int) = data[x + y * width]
    inline operator fun set(x: Int, y: Int, color: Color) {
        data[x + y * width] = color
    }

    inline fun flipVertically() = data.asIterable()
        .chunked(width)
        .asReversed()
        .flatten()
        .forEachIndexed { index, color ->
            data[index] = color
        }

    fun saveToDisk(): File {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val argbArray = data.map { it.argbColor }.toIntArray()
        bufferedImage.setRGB(0, 0, width, height, argbArray, 0, width)

        val file = Paths.get(".", "generated.png").toAbsolutePath().normalize().toFile()
        println("Image saved to : ${file.absolutePath}")
        ImageIO.write(bufferedImage, "png", file)
        return file
    }
}