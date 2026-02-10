package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.repository.CategoryRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class FindPageableCategoryUseCaseTest {

    private val categoryRepository = mockk<CategoryRepository>()
    private val tokenServicePort = mockk<TokenServicePort>()

    private lateinit var useCase: FindPageableCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase = FindPageableCategoryUseCase(
            categoryRepository = categoryRepository,
            tokenServicePort = tokenServicePort
        )
    }

    @Test
    fun `should return paginated categories when user is SYSTEM_ADMIN`() {
        val searchQuery = createSearchQuery(companyId = 1L)

        every {
            tokenServicePort.getClaimFromToken("role")
        } returns UserRole.SYSTEM_ADMIN.name

        every {
            tokenServicePort.getClaimFromToken("companyId")
        } returns null

        val pagination = Pagination(
            items = listOf(createCategory()),
            currentPage = 0,
            perPage = 10,
            totalItems = 1,
            totalPages = 1
        )

        every {
            categoryRepository.findBySearchQueryPageable(searchQuery)
        } returns pagination

        val result = useCase.execute(searchQuery)

        assertEquals(1, result.items.size)
        assertEquals(0, result.currentPage)
        assertEquals(10, result.perPage)
        assertEquals(1, result.totalItems)
        assertEquals(1, result.totalPages)

        verify(exactly = 1) {
            categoryRepository.findBySearchQueryPageable(searchQuery)
        }
    }

    @Test
    fun `should return paginated categories when user is not SYSTEM_ADMIN and companyId matches`() {
        val companyId = 1L
        val searchQuery = createSearchQuery(companyId = companyId)

        every {
            tokenServicePort.getClaimFromToken("role")
        } returns UserRole.COMPANY_ADMIN.name

        every {
            tokenServicePort.getClaimFromToken("companyId")
        } returns companyId.toString()

        val pagination = Pagination(
            items = listOf(createCategory(companyId)),
            currentPage = 0,
            perPage = 10,
            totalItems = 1,
            totalPages = 1
        )

        every {
            categoryRepository.findBySearchQueryPageable(searchQuery)
        } returns pagination

        val result = useCase.execute(searchQuery)

        assertEquals(1, result.items.size)
        assertEquals(companyId, result.items.first().companyId)

        verify(exactly = 1) {
            categoryRepository.findBySearchQueryPageable(searchQuery)
        }
    }

    @Test
    fun `should throw InvalidTokenException when role is missing in token`() {
        val searchQuery = createSearchQuery(companyId = 1L)

        every {
            tokenServicePort.getClaimFromToken("role")
        } returns null

        val exception = assertThrows<InvalidTokenException> {
            useCase.execute(searchQuery)
        }

        assertEquals("Usuário inválido.", exception.message)

        verify(exactly = 0) {
            categoryRepository.findBySearchQueryPageable(any())
        }
    }

    @Test
    fun `should throw WithoutPermissionException when token companyId is different from searchQuery companyId`() {
        val searchQuery = createSearchQuery(companyId = 2L)

        every {
            tokenServicePort.getClaimFromToken("role")
        } returns UserRole.COMPANY_ADMIN.name

        every {
            tokenServicePort.getClaimFromToken("companyId")
        } returns "1"

        val exception = assertThrows<WithoutPermissionException> {
            useCase.execute(searchQuery)
        }

        assertEquals(
            "Usuário sem permissão para acessar essa empresa.",
            exception.message
        )

        verify(exactly = 0) {
            categoryRepository.findBySearchQueryPageable(any())
        }
    }

    @Test
    fun `should throw WithoutPermissionException when searchQuery companyId is null for non SYSTEM_ADMIN`() {
        val searchQuery = createSearchQuery(companyId = null)

        every {
            tokenServicePort.getClaimFromToken("role")
        } returns UserRole.COMPANY_ADMIN.name

        every {
            tokenServicePort.getClaimFromToken("companyId")
        } returns "1"

        val exception = assertThrows<WithoutPermissionException> {
            useCase.execute(searchQuery)
        }

        assertEquals(
            "Usuário sem permissão para acessar essa empresa.",
            exception.message
        )

        verify(exactly = 0) {
            categoryRepository.findBySearchQueryPageable(any())
        }
    }

    private fun createSearchQuery(companyId: Long?) =
        CategorySearchQuery(
            companyId = companyId,
            page = 0,
            perPage = 10,
            terms = "",
            sort = "id",
            direction = Direction.ASC
        )

    private fun createCategory(companyId: Long = 1L) =
        Category(
            id = 1L,
            name = "Category",
            description = "Description",
            company = Company(id = companyId, name = "Company", cnpj = "12345678901234")
        )
}
