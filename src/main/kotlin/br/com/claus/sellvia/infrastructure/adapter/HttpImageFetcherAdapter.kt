package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.ImageFetcherPort
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import org.springframework.stereotype.Component
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

@Component
class HttpImageFetcherAdapter : ImageFetcherPort {
    companion object {
        private const val CONNECT_TIMEOUT_MS = 4_000
        private const val READ_TIMEOUT_MS = 8_000
        private const val OUTPUT_CONTENT_TYPE = "image/jpeg"
    }

    override fun fetchAsBase64DataUri(url: String): String? {
        if (url.isBlank()) return null

        return try {
            val rawBytes = downloadBytes(url) ?: return null

            // iText 2.x (usado pelo flying-saucer-pdf 9.x) não suporta WebP.
            // Convertemos qualquer imagem recebida para JPEG via Scrimage,
            // que lida com WebP automaticamente quando scrimage-webp está no classpath.
            val jpegBytes = convertToJpeg(rawBytes) ?: return null

            val base64 = Base64.getEncoder().encodeToString(jpegBytes)
            "data:$OUTPUT_CONTENT_TYPE;base64,$base64"
        } catch (e: Exception) {
            null
        }
    }

    private fun downloadBytes(url: String): ByteArray? {
        val connection = URL(url).openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.instanceFollowRedirects = true
            connection.setRequestProperty("User-Agent", "Sellvia-Catalog/1.0")
            connection.connect()

            if (connection.responseCode !in 200..299) return null

            connection.inputStream.use { it.readBytes() }
        } finally {
            connection.disconnect()
        }
    }

    private fun convertToJpeg(bytes: ByteArray): ByteArray? {
        return try {
            ImmutableImage.loader().fromBytes(bytes).bytes(JpegWriter.Default)
        } catch (e: Exception) {
            null
        }
    }
}