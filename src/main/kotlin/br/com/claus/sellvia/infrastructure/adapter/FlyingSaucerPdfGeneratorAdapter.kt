package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.PdfGeneratorPort
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class FlyingSaucerPdfGeneratorAdapter : PdfGeneratorPort {
    override fun generate(html: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        PdfRendererBuilder()
            .withHtmlContent(html, null)
            .toStream(outputStream)
            .run()
        return outputStream.toByteArray()
    }
}