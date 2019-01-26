package fr.oyashirox.model

import fr.oyashirox.math.Image
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
            return Texture(image)
        }
    }
}