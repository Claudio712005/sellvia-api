package br.com.claus.sellvia.application.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequestDTO(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password: String,
)
