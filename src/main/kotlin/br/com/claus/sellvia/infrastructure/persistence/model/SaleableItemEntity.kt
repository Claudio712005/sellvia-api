package br.com.claus.sellvia.infrastructure.persistence.model

import br.com.claus.sellvia.domain.enums.ResourceStatus
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "saleable_items", schema = "sellvia")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
@EntityListeners(AuditingEntityListener::class)
abstract class SaleableItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saleable_items_gen")
    @SequenceGenerator(
        name = "saleable_items_gen",
        sequenceName = "saleable_items_id_seq",
        allocationSize = 1
    )
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val productionCost: BigDecimal,
    val imageUrl: String?,
    @ManyToOne(fetch = FetchType.LAZY)
    val company: CompanyEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    val category: CategoryEntity? = null,
    @Enumerated(EnumType.STRING)
    val status: ResourceStatus,
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
    @CreatedBy
    var createdBy: String? = null,
    @LastModifiedBy
    var updatedBy: String? = null,
)