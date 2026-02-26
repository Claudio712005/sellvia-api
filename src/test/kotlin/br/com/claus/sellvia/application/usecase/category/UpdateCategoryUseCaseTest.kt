package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.repository.CategoryRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class UpdateCategoryUseCaseTest {

    private val repository = mockk<CategoryRepository>()
    private val tokenService = mockk<TokenServicePort>()

    private lateinit var useCase: UpdateCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase = UpdateCategoryUseCase(
            repository = repository,
            tokenService = tokenService
        )
    }

    @Test
    fun `should update category successfully when data is valid`() {
        val id = 1L
        val request = createValidRequest()
        val company = Company(id = 10L)
        val existingCategory = Category(id = id, name = "Old Name", description = "Old Desc", company = company)

        every { repository.findById(id) } returns existingCategory
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()
        every { repository.findByNameAndCompanyId(request.name!!, 10L) } returns null
        every { repository.save(any()) } returns existingCategory

        val response = useCase.execute(id, request)

        verify(exactly = 1) { repository.save(any()) }
        assertEquals(request.name, existingCategory.name)
        assertEquals(request.description, existingCategory.description)
    }

    @Test
    fun `should throw NotFoundResouceException when category does not exist`() {
        val id = 1L
        val request = createValidRequest()

        every { repository.findById(id) } returns null

        val exception = assertThrows<NotFoundResouceException> {
            useCase.execute(id, request)
        }

        assertEquals("Categoria com ID $id não encontrada", exception.message)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should throw WithoutPermissionException when user belongs to different company`() {
        val id = 1L
        val request = createValidRequest()
        val company = Company(id = 10L)
        val existingCategory = Category(id = id, company = company)

        every { repository.findById(id) } returns existingCategory
        every { tokenService.getClaimFromToken("companyId") } returns "99"
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()

        assertThrows<WithoutPermissionException> {
            useCase.execute(id, request)
        }

        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should allow update when user is SYSTEM_ADMIN even from different company`() {
        val id = 1L
        val request = createValidRequest()
        val company = Company(id = 10L)
        val existingCategory = Category(id = id, company = company)

        every { repository.findById(id) } returns existingCategory
        every { tokenService.getClaimFromToken("companyId") } returns "99"
        every { tokenService.getClaimFromToken("role") } returns UserRole.SYSTEM_ADMIN.toString()
        every { repository.findByNameAndCompanyId(request.name!!, 10L) } returns null
        every { repository.save(any()) } returns existingCategory

        useCase.execute(id, request)

        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `should throw IllegalArgumentException when new name already exists for another category`() {
        val id = 1L
        val request = createValidRequest()
        val company = Company(id = 10L)
        val existingCategory = Category(id = id, name = "Old Name", company = company)

        val otherCategory = Category(id = 2L, name = request.name!!, company = company)

        every { repository.findById(id) } returns existingCategory
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()

        every { repository.findByNameAndCompanyId(request.name!!, 10L) } returns otherCategory

        val exception = assertThrows<IllegalArgumentException> {
            useCase.execute(id, request)
        }

        assertEquals("Já existe uma categoria com o nome '${request.name}' para esta empresa", exception.message)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should not throw exception if findByName returns the same category being updated`() {
        val id = 1L
        val request = createValidRequest()
        val company = Company(id = 10L)
        val existingCategory = Category(id = id, name = request.name!!, company = company)

        every { repository.findById(id) } returns existingCategory
        every { tokenService.getClaimFromToken("companyId") } returns "10"
        every { tokenService.getClaimFromToken("role") } returns UserRole.COMPANY_USER.toString()
        every { repository.findByNameAndCompanyId(request.name!!, 10L) } returns existingCategory
        every { repository.save(any()) } returns existingCategory

        useCase.execute(id, request)

        verify(exactly = 1) { repository.save(any()) }
    }

    private fun createValidRequest() = CategoryRequestDTO(
        name = "Updated Category",
        description = "Updated description",
        companyId = 10L
    )
}