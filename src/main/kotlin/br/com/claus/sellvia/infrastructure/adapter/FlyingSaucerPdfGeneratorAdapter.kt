package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.PdfGeneratorPort
import org.springframework.stereotype.Component
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Component
class FlyingSaucerPdfGeneratorAdapter : PdfGeneratorPort {
    override fun generate(html: String): ByteArray {
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(html)
        renderer.layout()
        val outputStream = ByteArrayOutputStream()
        renderer.createPDF(outputStream)
        outputStream.close()
        return outputStream.toByteArray()
    }
}