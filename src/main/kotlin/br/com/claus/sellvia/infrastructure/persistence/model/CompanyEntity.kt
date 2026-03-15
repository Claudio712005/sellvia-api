package br.com.claus.sellvia.infrastructure.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "companies", schema = "sellvia")
@EntityListeners(AuditingEntityListener::class)
data class CompanyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companies_gen")
    @SequenceGenerator(
        name = "companies_gen",
        sequenceName = "sellvia.companies_id_seq",
        allocationSize = 1,
    )
    val id: Long? = null,
    val name: String = "",
    val cnpj: String = "",
    val businessName: String = "",
    val websiteUrl: String = "",
    val isActive: Boolean = true,
    val companyUrlLogo: String = "",
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
    @CreatedBy
    var createdBy: String? = null,
    @LastModifiedBy
    var updatedBy: String? = null,
) {
    @OneToMany(mappedBy = "company")
    val users: List<UserEntity> = emptyList()
}