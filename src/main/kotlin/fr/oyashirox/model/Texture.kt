package fr.oyashirox.model

import fr.oyashirox.math.Image
import fr.oyashirox.math.Vector
import java.io.File
import javax.imageio.ImageIO

class Texture(val image: Image) {
    companion object {
        fun loadFromFile(file: File): Texture {
            val bufferedImage = ImageIO.read(file)
            val array = bufferedImage.getRGB(
                0, 0, bufferedImage.width, bufferedImage.height,
                null, 0, bufferedImage.width
            )
            val image = Image(bufferedImage.width, bufferedImage.height, array)
            image.flipVertically()
            return Texture(image)
        }
    }

    operator fun get(x: Double, y: Double) = image[
            (x * (image.width - 1)).toInt(),
            (y * (image.height - 1)).toInt()
    ]

    operator fun get(vector: Vector) = image[
            (vector.x * (image.width - 1)).toInt(),
            (vector.y * (image.height - 1)).toInt()
    ]
}