package br.com.claus.sellvia.application.dto

data class OptimizedImageDTO(
    val bytes: ByteArray,
    val extension: String,
    val contentType: String
)