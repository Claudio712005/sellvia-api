package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.AuthenticatedUserDetails
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
    private val permissionHelperPort = mockk<PermissionHelperPort>()

    private lateinit var useCase: FindPageableCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase = FindPageableCategoryUseCase(
            categoryRepository = categoryRepository,
            permissionHelperPort = permissionHelperPort
        )
    }

    @Test
    fun `should return all categories when user is SYSTEM_ADMIN`() {
        val searchQuery = createSearchQuery(companyId = null)
        val adminDetails = AuthenticatedUserDetails(role = UserRole.SYSTEM_ADMIN, companyId = null)

        every { permissionHelperPort.getDetailsOfAuthenticatedUser() } returns adminDetails
        every { permissionHelperPort.verifyUserCanDoesThisAction(any()) } just runs

        val pagination = Pagination(
            items = listOf(createCategory()),
            currentPage = 0, perPage = 10, totalItems = 1, totalPages = 1
        )

        every { categoryRepository.findBySearchQueryPageable(any()) } returns pagination

        val result = useCase.execute(searchQuery)

        assertEquals(1, result.items.size)
        verify(exactly = 1) { categoryRepository.findBySearchQueryPageable(match { it.companyId == null }) }
    }

    @Test
    fun `should enforce company filter from user details when not admin`() {
        // Cenário: Usuário comum tenta buscar sem filtro, mas o UseCase deve injetar a empresa 10L
        val userCompanyId = 10L
        val searchQuery = createSearchQuery(companyId = null)
        val userDetails = AuthenticatedUserDetails(role = UserRole.COMPANY_USER, companyId = userCompanyId)

        every { permissionHelperPort.getDetailsOfAuthenticatedUser() } returns userDetails
        every { permissionHelperPort.verifyUserCanDoesThisAction(userCompanyId) } just runs

        val pagination = Pagination(
            items = listOf(createCategory(userCompanyId)),
            currentPage = 0, perPage = 10, totalItems = 1, totalPages = 1
        )

        every {
            categoryRepository.findBySearchQueryPageable(match { it.companyId == userCompanyId })
        } returns pagination

        useCase.execute(searchQuery)

        verify(exactly = 1) {
            categoryRepository.findBySearchQueryPageable(match { it.companyId == userCompanyId })
        }
    }

    @Test
    fun `should throw WithoutPermissionException when permission helper denies action`() {
        val userCompanyId = 10L
        val targetCompanyId = 99L
        val searchQuery = createSearchQuery(companyId = targetCompanyId)

        // Simula usuário logado na empresa 10 tentando acessar a 99
        val userDetails = AuthenticatedUserDetails(role = UserRole.COMPANY_USER, companyId = userCompanyId)

        every { permissionHelperPort.getDetailsOfAuthenticatedUser() } returns userDetails

        every {
            permissionHelperPort.verifyUserCanDoesThisAction(any())
        } throws WithoutPermissionException("Acesso negado")

        assertThrows<WithoutPermissionException> {
            useCase.execute(searchQuery)
        }

        verify(exactly = 0) { categoryRepository.findBySearchQueryPageable(any()) }
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