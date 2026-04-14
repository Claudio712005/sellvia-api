package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.infrastructure.persistence.model.CategoryEntity
import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity
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

class CategoryRepositoryImplTest : AbstractRepositoryTest() {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: CategoryRepositoryImpl

    private lateinit var savedCompany: CompanyEntity

    @BeforeEach
    fun setup() {
        savedCompany = entityManager.persistAndFlush(
            CompanyEntity(
                name = "Category Company",
                cnpj = "44.444.444/0001-44",
                businessName = "Category Business",
                websiteUrl = "https://category.com",
                isActive = true,
            )
        )
    }

    @Test
    fun `should save and find category by id`() {
        val saved = repository.save(buildCategory("Electronics"))

        saved.id.shouldNotBeNull()
        val found = repository.findById(saved.id!!)
        found.shouldNotBeNull()
        found.name shouldBe "Electronics"
    }

    @Test
    fun `should return null when category not found`() {
        repository.findById(999L).shouldBeNull()
    }

    @Test
    fun `should delete category by id`() {
        val saved = repository.save(buildCategory("To Delete"))

        repository.deleteById(saved.id!!)
        entityManager.flush()
        entityManager.clear()

        repository.findById(saved.id!!).shouldBeNull()
    }

    @Test
    fun `should find category by name and company id`() {
        repository.save(buildCategory("Furniture"))

        val found = repository.findByNameAndCompanyId("Furniture", savedCompany.id!!)
        found.shouldNotBeNull()
        found.name shouldBe "Furniture"

        repository.findByNameAndCompanyId("NonExistent", savedCompany.id!!).shouldBeNull()
    }

    @Test
    fun `should verify category exists by id and company`() {
        val saved = repository.save(buildCategory("Verified"))

        repository.existsByIdAndCompanyId(saved.id!!, savedCompany.id!!).shouldBeTrue()
        repository.existsByIdAndCompanyId(saved.id!!, 999L).shouldBeFalse()
        repository.existsByIdAndCompanyId(999L, savedCompany.id!!).shouldBeFalse()
    }

    @Test
    fun `should list all categories`() {
        repository.save(buildCategory("Cat A"))
        repository.save(buildCategory("Cat B"))

        val all = repository.findAll()
        all.size shouldBe 2
    }

    @Test
    fun `should paginate categories filtered by company`() {
        repository.save(buildCategory("Paginated A"))
        repository.save(buildCategory("Paginated B"))

        val query = CategorySearchQuery(
            page = 0,
            perPage = 10,
            sort = "id",
            direction = Direction.ASC,
            companyId = savedCompany.id!!,
            name = "",
        )

        val result = repository.findBySearchQueryPageable(query)
        result.items shouldHaveSize 2
        result.totalItems shouldBe 2
    }

    @Test
    fun `should filter categories by name in paginated search`() {
        repository.save(buildCategory("Alpha Category"))
        repository.save(buildCategory("Beta Category"))

        val query = CategorySearchQuery(
            page = 0,
            perPage = 10,
            sort = "id",
            direction = Direction.ASC,
            companyId = savedCompany.id!!,
            name = "Alpha",
        )

        val result = repository.findBySearchQueryPageable(query)
        result.items shouldHaveSize 1
        result.items.first().name shouldBe "Alpha Category"
    }

    private fun buildCategory(name: String) =
        Category(
            id = null,
            name = name,
            description = "Test description",
            company = Company(id = savedCompany.id),
        )
}
