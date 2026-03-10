package br.com.claus.sellvia.infrastructure.persistence.mapper

import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity

fun Company.toEntity(): CompanyEntity {
    return CompanyEntity(
        id = this.id,
        name = this.name,
        cnpj = this.cnpj,
        businessName = this.businessName,
        websiteUrl = this.websiteUrl,
        isActive = this.isActive,
        companyUrlLogo = this.companyUrlLogo,
    )
}

fun CompanyEntity.toDomain(): Company {
    return Company(
        id = this.id,
        name = this.name,
        cnpj = this.cnpj,
        businessName = this.businessName,
        websiteUrl = this.websiteUrl,
        isActive = this.isActive,
        companyUrlLogo = this.companyUrlLogo,
    )
}