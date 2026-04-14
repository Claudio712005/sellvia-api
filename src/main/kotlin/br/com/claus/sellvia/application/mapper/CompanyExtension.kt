package br.com.claus.sellvia.application.mapper

import br.com.claus.sellvia.application.dto.response.CompanyResponseDTO
import br.com.claus.sellvia.domain.model.Company

fun Company.toResponseDTO(): CompanyResponseDTO {
    return CompanyResponseDTO(
        id = this.id,
        name = this.name,
        websiteUrl = this.websiteUrl,
        companyUrlLogo = this.companyUrlLogo ?: "",
    )
}