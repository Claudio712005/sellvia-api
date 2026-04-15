package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.exception.EntitiesConflictException
import br.com.claus.sellvia.domain.exception.InvalidFieldException
import br.com.claus.sellvia.domain.model.Category
import br.com.claus.sellvia.domain.repository.CategoryRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateCategoryUseCaseTest {
    private val repository = mockk<CategoryRepository>()
    private val permissionHelperPort = mockk<PermissionHelperPort>()

    private lateinit var useCase: CreateCategoryUseCase

    @BeforeEach
    fun setup() {
        useCase =
            CreateCategoryUseCase(
                repository = repository,
                permissionServiceHelperPort = permissionHelperPort
            )
    }

    @Test
    fun `should create category successfully when data is valid`() {
        val request = createValidRequest()

        every {
            repository.findByNameAndCompanyId(request.name!!, request.companyId!!)
        } returns null

        every {
            permissionHelperPort.verifyUserCanDoesThisAction(request.companyId!!)
        } just runs

        every {
            repository.save(any<Category>())
        } returns mockk()

        useCase.execute(request)

        verify(exactly = 1) {
            repository.save(any<Category>())
        }
    }

    @Test
    fun `should throw InvalidFieldException when request validation fails`() {
        val request =
            CategoryRequestDTO(
                name = null,
                description = "Description",
                companyId = 1L
            )

        assertThrows<InvalidFieldException> {
            useCase.execute(request)
        }

        verify(exactly = 0) {
            repository.save(any())
        }
    }

    @Test
    fun `should throw EntitiesConflictException when category name already exists for company`() {
        val request = createValidRequest()

        every {
            repository.findByNameAndCompanyId(request.name!!, request.companyId!!)
        } returns mockk()

        val exception =
            assertThrows<EntitiesConflictException> {
                useCase.execute(request)
            }

        assertEquals(
            "Já existe uma categoria com o nome '${request.name}' para esta empresa.",
            exception.message
        )

        verify(exactly = 0) {
            repository.save(any())
        }
    }

    private fun createValidRequest() =
        CategoryRequestDTO(
            name = "Category Test",
            description = "Category description",
            companyId = 1L
        )
}