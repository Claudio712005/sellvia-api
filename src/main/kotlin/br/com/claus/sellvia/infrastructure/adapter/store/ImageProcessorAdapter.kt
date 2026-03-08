package br.com.claus.sellvia.infrastructure.adapter.store

import br.com.claus.sellvia.application.dto.OptimizedImageDTO
import br.com.claus.sellvia.application.port.store.ImageProcessorPort
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import org.springframework.stereotype.Component
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

@Component
class ImageProcessorAdapter: ImageProcessorPort{

    override fun optimize(imageBytes: ByteArray): OptimizedImageDTO {
        try {
            val image = ImmutableImage.loader().fromStream(ByteArrayInputStream(imageBytes))

            val resized = image.scaleToWidth(1080)

            val writer = WebpWriter.DEFAULT
                .withLossless()
                .withZ(6)

            val webpBytes = resized.bytes(writer)
            return OptimizedImageDTO(
                bytes = webpBytes,
                extension = "webp",
                contentType = "image/webp"
            )
        } catch (e: Exception) {
            throw RuntimeException("Falha ao processar imagem para WebP: ${e.message}", e)
        }
    }

    private fun resizeImage(originalImage: BufferedImage, targetWidth: Int): BufferedImage {
        val percentage = targetWidth.toDouble() / originalImage.width
        val targetHeight = (originalImage.height * percentage).toInt()

        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)

        val graphics2D: Graphics2D = resizedImage.createGraphics()

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null)
        graphics2D.dispose()

        return resizedImage
    }
}