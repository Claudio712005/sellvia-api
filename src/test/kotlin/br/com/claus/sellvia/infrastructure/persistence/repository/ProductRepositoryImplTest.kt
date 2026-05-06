package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal

class ProductRepositoryImplTest : AbstractRepositoryTest() {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: ProductRepositoryImpl

    private lateinit var savedCompany: CompanyEntity

    @BeforeEach
    fun setup() {
        savedCompany =
            entityManager.persistAndFlush(
                CompanyEntity(
                    name = "Test Company",
                    cnpj = "00.000.000/0001-00",
                    businessName = "Test Business",
                    websiteUrl = "https://test.com",
                    isActive = true,
                )
            )
    }

    @Test
    fun `should create and find product by id`() {
        val product = buildProduct("SKU-001", "Monitor")

        val created = repository.create(product)

        created.id.shouldNotBeNull()
        val found = repository.findById(created.id!!)
        found.shouldNotBeNull()
        found.name shouldBe "Monitor"
        found.sku shouldBe "SKU-001"
    }

    @Test
    fun `should return null when product not found by id`() {
        repository.findById(999L).shouldBeNull()
    }

    @Test
    fun `should return true when product exists by id`() {
        val created = repository.create(buildProduct("SKU-002", "Keyboard"))
        repository.existsById(created.id!!).shouldBeTrue()
    }

    @Test
    fun `should return false when product does not exist by id`() {
        repository.existsById(999L).shouldBeFalse()
    }

    @Test
    fun `should detect existing SKU for same company`() {
        repository.create(buildProduct("SKU-DUP", "Product A"))

        repository.existsBySkuAndCompanyId("SKU-DUP", savedCompany.id!!).shouldBeTrue()
        repository.existsBySkuAndCompanyId("SKU-OTHER", savedCompany.id!!).shouldBeFalse()
    }

    @Test
    fun `should detect existing name for same company`() {
        repository.create(buildProduct("SKU-003", "Unique Name"))

        repository.existsByNameAndCompanyId("Unique Name", savedCompany.id!!).shouldBeTrue()
        repository.existsByNameAndCompanyId("Other Name", savedCompany.id!!).shouldBeFalse()
    }

    @Test
    fun `existsBySkuAndCompanyIdAndNotId should exclude the product being updated`() {
        val product = repository.create(buildProduct("SKU-UPD", "Update Product"))
        val id = product.id!!

        repository.existsBySkuAndCompanyIdAndNotId("SKU-UPD", savedCompany.id!!, id).shouldBeFalse()

        val other = repository.create(buildProduct("SKU-OTHER2", "Other Product"))
        repository.existsBySkuAndCompanyIdAndNotId("SKU-OTHER2", savedCompany.id!!, id).shouldBeTrue()
    }

    @Test
    fun `existsByNameAndCompanyIdAndNotId should exclude the product being updated`() {
        val product = repository.create(buildProduct("SKU-004", "Target Product"))
        val id = product.id!!

        repository.existsByNameAndCompanyIdAndNotId("Target Product", savedCompany.id!!, id).shouldBeFalse()

        val other = repository.create(buildProduct("SKU-005", "Conflicting Name"))
        repository.existsByNameAndCompanyIdAndNotId("Conflicting Name", savedCompany.id!!, id).shouldBeTrue()
    }

    @Test
    fun `should update product fields`() {
        val created = repository.create(buildProduct("SKU-006", "Old Name"))

        val updated =
            repository.update(
                created.copy(
                    name = "New Name",
                    price = BigDecimal("999.00"),
                    externalLink = "https://instagram.com/test",
                    whatsappMessage = "Custom message",
                )
            )

        updated.name shouldBe "New Name"
        updated.price shouldBe BigDecimal("999.00")
        updated.externalLink shouldBe "https://instagram.com/test"
        updated.whatsappMessage shouldBe "Custom message"
    }

    @Test
    fun `should throw when updating product without id`() {
        shouldThrow<IllegalArgumentException> {
            repository.update(buildProduct("SKU-007", "No ID"))
        }
    }

    @Test
    fun `should delete product by id`() {
        val created = repository.create(buildProduct("SKU-008", "To Delete"))

        repository.delete(created.id!!)

        repository.existsById(created.id!!).shouldBeFalse()
    }

    @Test
    fun `should find product by SKU and company id`() {
        repository.create(buildProduct("SKU-FIND", "Findable"))

        val found = repository.findBySkuAndCompanyId("SKU-FIND", savedCompany.id!!)
        found.shouldNotBeNull()
        found.name shouldBe "Findable"

        repository.findBySkuAndCompanyId("SKU-MISSING", savedCompany.id!!).shouldBeNull()
    }

    @Test
    fun `should list products with pagination filtered by company`() {
        repository.create(buildProduct("SKU-P1", "Alpha Product"))
        repository.create(buildProduct("SKU-P2", "Beta Product"))

        val query = ProductSearchQuery(companyId = savedCompany.id!!, perPage = 10)
        val page = repository.findAll(query)

        page.items shouldHaveSize 2
        page.totalItems shouldBe 2
    }

    @Test
    fun `should filter products by name`() {
        repository.create(buildProduct("SKU-F1", "Alpha Product"))
        repository.create(buildProduct("SKU-F2", "Beta Product"))

        val query = ProductSearchQuery(companyId = savedCompany.id!!, name = "alpha")
        val page = repository.findAll(query)

        page.items shouldHaveSize 1
        page.items.first().name shouldBe "Alpha Product"
    }

    private fun buildProduct(
        sku: String,
        name: String,
        category: Category? = null,
    ) = Product(
        id = null,
        sku = sku,
        name = name,
        description = "Test description",
        price = BigDecimal("100.00"),
        productionCost = BigDecimal("50.00"),
        stockQuantity = 10,
        type = ProductType.PHYSICAL,
        company = Company(id = savedCompany.id),
        status = ResourceStatus.ACTIVE,
        imageUrl = null,
        category = category,
        externalLink = null,
        whatsappMessage = null,
    )
}