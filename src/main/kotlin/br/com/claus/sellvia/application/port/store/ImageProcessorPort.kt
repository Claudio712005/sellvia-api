package br.com.claus.sellvia.application.port.store

import br.com.claus.sellvia.application.dto.OptimizedImageDTO

interface ImageProcessorPort {
    fun optimize(imageBytes: ByteArray): OptimizedImageDTO
}