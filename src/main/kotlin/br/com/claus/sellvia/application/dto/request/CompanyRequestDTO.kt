package br.com.claus.sellvia.application.dto.request

data class CompanyRequestDTO(
    val id: Long,
    val name: String,
    val websiteUrl: String = "",
    val mainPhoneNumber: String = "",
    val isActive: Boolean = true,
)