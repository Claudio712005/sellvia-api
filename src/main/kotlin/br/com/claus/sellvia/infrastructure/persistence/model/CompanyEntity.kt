package br.com.claus.sellvia.infrastructure.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "companies")
data class CompanyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String = "",
    val cnpj: String = "",
    val businessName: String = "",
    val websiteUrl: String = "",
    val isActive: Boolean = true,
    val companyUrlLogo: String = "",
){

    @OneToMany(mappedBy = "company")
    val users: List<UserEntity> = emptyList()
}
