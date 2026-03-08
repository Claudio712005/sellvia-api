package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.AuthenticatedUserDetails
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
    private val permissionHelperPort = mockk<PermissionHelperPort>()

    private lateinit var useCase: UpdateCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase = UpdateCategoryUseCase(
            repository = repository,
            permissionHelperPort = permissionHelperPort,
        )
    }

    @Test
    fun `should update category successfully when data is valid`() {
        val id = 1L
        val request = createValidRequest()
        val companyId = 10L
        val existingCategory = Category(id = id, name = "Old Name", company = Company(id = companyId))

        every { repository.findById(id) } returns existingCategory
        every { permissionHelperPort.verifyUserCanDoesThisAction(companyId) } just Runs
        every { repository.findByNameAndCompanyId(request.name!!, companyId) } returns null
        every { repository.save(any()) } returns existingCategory

        val response = useCase.execute(id, request)

        verify(exactly = 1) { repository.save(any()) }
        assertEquals(request.name, existingCategory.name)
        verify { permissionHelperPort.verifyUserCanDoesThisAction(companyId) }
    }

    @Test
    fun `should throw NotFoundResouceException when category does not exist`() {
        val id = 1L
        val request = createValidRequest()

        every { repository.findById(id) } returns null

        assertThrows<NotFoundResouceException> {
            useCase.execute(id, request)
        }

        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should throw WithoutPermissionException when permission helper denies access`() {
        val id = 1L
        val request = createValidRequest()
        val companyId = 10L
        val existingCategory = Category(id = id, company = Company(id = companyId))

        every { repository.findById(id) } returns existingCategory
        every {
            permissionHelperPort.verifyUserCanDoesThisAction(companyId)
        } throws WithoutPermissionException("Acesso negado")

        assertThrows<WithoutPermissionException> {
            useCase.execute(id, request)
        }

        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should throw IllegalArgumentException when new name already exists for another category`() {
        val id = 1L
        val request = createValidRequest()
        val companyId = 10L
        val existingCategory = Category(id = id, name = "Old Name", company = Company(id = companyId))
        val otherCategory = Category(id = 2L, name = request.name!!, company = Company(id = companyId))

        every { repository.findById(id) } returns existingCategory
        every { permissionHelperPort.verifyUserCanDoesThisAction(companyId) } just Runs
        every { repository.findByNameAndCompanyId(request.name!!, companyId) } returns otherCategory

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
        val companyId = 10L
        val existingCategory = Category(id = id, name = request.name!!, company = Company(id = companyId))

        every { repository.findById(id) } returns existingCategory
        every { permissionHelperPort.verifyUserCanDoesThisAction(companyId) } just Runs
        every { repository.findByNameAndCompanyId(request.name!!, companyId) } returns existingCategory
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