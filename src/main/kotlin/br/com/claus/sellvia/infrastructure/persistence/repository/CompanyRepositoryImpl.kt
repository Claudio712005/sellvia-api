package br.com.claus.sellvia.infrastructure.persistence.repository

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
}