package br.com.claus.sellvia.infrastructure.persistence.jpa

import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataCompanyRepository : JpaRepository<CompanyEntity, Long>