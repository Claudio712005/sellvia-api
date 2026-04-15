package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.repository.CompanyRepository
import br.com.claus.sellvia.infrastructure.persistence.jpa.SpringDataCompanyRepository
import br.com.claus.sellvia.infrastructure.persistence.mapper.toDomain
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class CompanyRepositoryImpl(
    private val springDataRepository: SpringDataCompanyRepository,
) : CompanyRepository {
    override fun findById(id: Long): Company? {
        return springDataRepository.findById(id).getOrNull()?.toDomain()
    }

    override fun update(company: Company): Company {
        val entity =
            springDataRepository.findById(company.id!!).getOrNull()
                ?: throw NotFoundResouceException("Empresa com id '${company.id}' não encontrada.")

        if (existsByCnpjAndIdNot(company.cnpj, company.id!!)) {
            throw NotFoundResouceException("Empresa com cnpj '${company.cnpj}' já existe.")
        }

        val updatedEntity =
            entity.copy(
                name = company.name,
                cnpj = company.cnpj,
                businessName = company.businessName,
                websiteUrl = company.websiteUrl,
                isActive = company.isActive,
                companyUrlLogo = company.companyUrlLogo,
                mainPhoneNumber = company.mainPhoneNumber
            )

        return springDataRepository.save(updatedEntity).toDomain()
    }

    override fun existsByCnpjAndIdNot(
        cnpj: String,
        id: Long,
    ): Boolean = springDataRepository.existsByCnpjAndIdNot(cnpj, id)

    override fun existsByMainPhoneNumberAndIdNot(
        mainPhone: String,
        id: Long,
    ): Boolean = springDataRepository.existsByMainPhoneNumberAndIdNot(mainPhone, id)
}