package br.com.claus.sellvia.infrastructure.persistence.repository

import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.infrastructure.persistence.model.CompanyEntity
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

class CompanyRepositoryImplTest : AbstractRepositoryTest() {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: CompanyRepositoryImpl

    private lateinit var savedCompany: CompanyEntity

    @BeforeEach
    fun setup() {
        savedCompany =
            entityManager.persistAndFlush(
                CompanyEntity(
                    name = "Original Company",
                    cnpj = "11.111.111/0001-11",
                    businessName = "Original Business",
                    websiteUrl = "https://original.com",
                    isActive = true,
                    mainPhoneNumber = "11999990000",
                )
            )
    }

    @Test
    fun `should find company by id`() {
        val found = repository.findById(savedCompany.id!!)

        found.shouldNotBeNull()
        found.name shouldBe "Original Company"
        found.cnpj shouldBe "11.111.111/0001-11"
    }

    @Test
    fun `should return null when company not found`() {
        repository.findById(999L).shouldBeNull()
    }

    @Test
    fun `should update company fields`() {
        val updated =
            repository.update(
                Company(
                    id = savedCompany.id,
                    name = "Updated Company",
                    cnpj = "11.111.111/0001-11",
                    businessName = "Updated Business",
                    websiteUrl = "https://updated.com",
                    isActive = true,
                    companyUrlLogo = null,
                    mainPhoneNumber = "11988880000",
                )
            )

        updated.name shouldBe "Updated Company"
        updated.businessName shouldBe "Updated Business"
        updated.mainPhoneNumber shouldBe "11988880000"
    }

    @Test
    fun `should return true when CNPJ exists for different company`() {
        val other =
            entityManager.persistAndFlush(
                CompanyEntity(
                    name = "Other Company",
                    cnpj = "22.222.222/0001-22",
                    businessName = "Other Business",
                    websiteUrl = "https://other.com",
                    isActive = true,
                )
            )

        repository.existsByCnpjAndIdNot("22.222.222/0001-22", savedCompany.id!!).shouldBeTrue()
    }

    @Test
    fun `should return false when CNPJ belongs to the same company`() {
        repository.existsByCnpjAndIdNot("11.111.111/0001-11", savedCompany.id!!).shouldBeFalse()
    }

    @Test
    fun `should return true when phone exists for different company`() {
        entityManager.persistAndFlush(
            CompanyEntity(
                name = "Another Company",
                cnpj = "33.333.333/0001-33",
                businessName = "Another Business",
                websiteUrl = "https://another.com",
                isActive = true,
                mainPhoneNumber = "11977770000",
            )
        )

        repository.existsByMainPhoneNumberAndIdNot("11977770000", savedCompany.id!!).shouldBeTrue()
    }

    @Test
    fun `should return false when phone belongs to the same company`() {
        repository.existsByMainPhoneNumberAndIdNot("11999990000", savedCompany.id!!).shouldBeFalse()
    }
}