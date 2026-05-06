package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.mapper.toDomain
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.ProcessorResourceStorePort
import br.com.claus.sellvia.domain.enums.FolderDestination
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.repository.CategoryRepository
import br.com.claus.sellvia.domain.repository.ProductRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class CreateProductUseCaseTest {
    private val repository = mockk<ProductRepository>()
    private val permissionHelpPort = mockk<PermissionHelperPort>()
    private val processorResourceStorePort = mockk<ProcessorResourceStorePort>()
    private val categoryRepository = mockk<CategoryRepository>()

    private lateinit var useCase: CreateProductUseCase

    @BeforeEach
    fun setup() {
        useCase =
            CreateProductUseCase(
                repository = repository,
                permissionServicePort = permissionHelpPort,
                processorResourceStorePort = processorResourceStorePort,
                categoryRepository = categoryRepository,
            )
    }

    @Test
    fun `should create product successfully when data is valid`() {
        val request = createValidRequest(categoryId = 10L)
        val image = "fake-image".toByteArray()
        val productSaved = request.toDomain().copy(id = 1L)
        val imageUrl = "path/to/image.jpg"

        every { permissionHelpPort.verifyUserCanDoesThisAction(request.companyId) } just runs
        every { repository.existsBySkuAndCompanyId(any(), any()) } returns false
        every { repository.existsByNameAndCompanyId(any(), any()) } returns false

        every { categoryRepository.existsByIdAndCompanyId(10L, request.companyId) } returns true

        every { repository.create(any()) } returns productSaved
        every { processorResourceStorePort.saveOptimizedImage(any(), any(), any(), any()) } returns imageUrl
        every { repository.update(any()) } returns mockk()

        useCase.execute(request, image)

        verify(exactly = 1) { repository.create(any()) }
        verify(exactly = 1) { categoryRepository.existsByIdAndCompanyId(10L, request.companyId) }
        verify(exactly = 1) { processorResourceStorePort.saveOptimizedImage(image, request.companyId, FolderDestination.PRODUCT, 1L) }
        verify(exactly = 1) { repository.update(match { it.imageUrl == imageUrl }) }
    }

    @Test
    fun `should throw NotFoundResouceException when category does not exist for the company`() {
        val request = createValidRequest(categoryId = 999L)

        every { permissionHelpPort.verifyUserCanDoesThisAction(request.companyId) } just runs
        every { repository.existsBySkuAndCompanyId(any(), any()) } returns false
        every { repository.existsByNameAndCompanyId(any(), any()) } returns false

        every { categoryRepository.existsByIdAndCompanyId(999L, request.companyId) } returns false

        val exception =
            assertThrows<NotFoundResouceException> {
                useCase.execute(request, byteArrayOf())
            }

        assertEquals("Categoria com id '999' não encontrada para esta empresa.", exception.message)
        verify(exactly = 0) { repository.create(any()) }
    }

    @Test
    fun `should throw exception and rollback when image upload fails`() {
        val request = createValidRequest()
        val image = "fake-image".toByteArray()
        val productSaved =
            mockk<Product> {
                every { id } returns 1L
            }

        every { permissionHelpPort.verifyUserCanDoesThisAction(any()) } just runs
        every { repository.existsBySkuAndCompanyId(any(), any()) } returns false
        every { repository.existsByNameAndCompanyId(any(), any()) } returns false
        every { repository.create(any()) } returns productSaved

        every { processorResourceStorePort.saveOptimizedImage(any(), any(), any(), any()) } throws RuntimeException("S3 Error")
        every { repository.delete(1L) } just runs

        assertThrows<RuntimeException> {
            useCase.execute(request, image)
        }

        verify(exactly = 1) { repository.delete(1L) }
    }

    @Test
    fun `should throw ResourceAlreadyExistsException when SKU already exists`() {
        val request = createValidRequest()

        every { permissionHelpPort.verifyUserCanDoesThisAction(request.companyId) } just runs
        every { repository.existsBySkuAndCompanyId(request.sku.uppercase(), request.companyId) } returns true

        val exception =
            assertThrows<ResourceAlreadyExistsException> {
                useCase.execute(request, byteArrayOf())
            }
        assertEquals("Produto com SKU '${request.sku.uppercase()}' já existe para esta empresa.", exception.message)
        verify(exactly = 0) { repository.create(any()) }
    }

    private fun createValidRequest(categoryId: Long? = null) =
        ProductRequestDTO(
            name = "Teclado Mecânico",
            description = "RGB Cherry MX Blue",
            price = BigDecimal("500.00"),
            productionCost = BigDecimal("200.00"),
            companyId = 123L,
            sku = "KBD-123",
            categoryId = categoryId
        )
}