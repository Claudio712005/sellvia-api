package br.com.claus.sellvia.application.port

interface PdfGeneratorPort {
    fun generate(html: String): ByteArray
}