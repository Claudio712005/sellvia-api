package br.com.claus.sellvia.domain.model

data class Company(
    val id: Long? = null,
    val name: String = "",
    val cnpj: String = "",
    val businessName: String = "",
    val websiteUrl: String = "",
    val isActive: Boolean = true,
    var companyUrlLogo: String? = null,
    val mainPhoneNumber: String = "",
)