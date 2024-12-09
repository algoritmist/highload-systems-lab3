package itmo.highload.service

import itmo.highload.api.dto.PlaceImage
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.min
import java.awt.image.BufferedImage

@Service
class PlaceImageService(
    private val storage: ImageStorage
) {
    private val imageSizePixels = 100
    fun processImage(placeId: String, imageBytes: ByteArray): PlaceImage{
        val imageIS = ByteArrayInputStream(imageBytes)
        val image = ImageIO.read(imageIS)
        val minSize = min(image.width, image.height)

        val cutImage = image.getSubimage(0, 0, minSize, minSize)
        val resizedImage = BufferedImage(imageSizePixels, imageSizePixels, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.graphics
        graphics.drawImage(cutImage, 0, 0, imageSizePixels, imageSizePixels, null)
        graphics.dispose()

        val buffer = ByteArrayOutputStream()
        ImageIO.write(resizedImage, "jpg", buffer)
        buffer.flush()
        buffer.close()

        val url = storage.storeFile("place-image-$placeId-${System.currentTimeMillis()}", buffer.toByteArray())
        return PlaceImage(placeId, url)
    }
}