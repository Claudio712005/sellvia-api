package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.ResourceAlreadyExistsException
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class UpdateProductUseCaseTest {
    private val repository = mock(ProductRepository::class.java)
    private val permissionHelperPort = mock(PermissionHelperPort::class.java)
    private val useCase = UpdateProductUseCase(repository, permissionHelperPort)

    @Test
    fun `should update product successfully when all validations pass`() {
        val productId = 1L
        val companyId = 10L
        val request = createRequestDTO(companyId)
        val existingProduct = createDomainProduct(productId, companyId)

        `when`(repository.findById(productId)).thenReturn(existingProduct)
        `when`(repository.existsBySkuAndCompanyIdAndNotId(anyString(), anyLong(), anyLong())).thenReturn(false)
        `when`(repository.existsByNameAndCompanyIdAndNotId(anyString(), anyLong(), anyLong())).thenReturn(false)
        `when`(repository.update(any(Product::class.java))).thenAnswer { it.arguments[0] as Product }

        val response = useCase.execute(request, productId)

        assertEquals(request.name, response.name)
        assertEquals(request.sku.uppercase(), response.sku)
        verify(permissionHelperPort).verifyUserCanDoesThisAction(companyId)
        verify(repository).update(any(Product::class.java))
    }

    @Test
    fun `should throw IllegalArgumentException when id is invalid`() {
        val request = createRequestDTO()

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                useCase.execute(request, 0L)
            }

        assertEquals("ID do produto deve ser um número positivo.", exception.message)
    }

    @Test
    fun `should throw NotFoundResourceException when product does not exist`() {
        val productId = 1L
        val request = createRequestDTO()

        `when`(repository.findById(productId)).thenReturn(null)

        assertThrows(NotFoundResouceException::class.java) {
            useCase.execute(request, productId)
        }
    }

    @Test
    fun `should throw ResourceAlreadyExistsException when SKU already taken`() {
        val productId = 1L
        val companyId = 10L
        val request = createRequestDTO(companyId)

        `when`(repository.existsBySkuAndCompanyIdAndNotId(anyString(), anyLong(), anyLong())).thenReturn(true)

        assertThrows(ResourceAlreadyExistsException::class.java) {
            useCase.execute(request, productId)
        }
    }

    private fun createRequestDTO(companyId: Long = 10L) =
        ProductRequestDTO(
            name = "Novo Nome",
            description = "Nova Descricao",
            price = BigDecimal("100.0"),
            productionCost = BigDecimal("50.0"),
            companyId = companyId,
            sku = "SKU123",
            stockQuantity = 10,
            type = ProductType.PHYSICAL,
            status = ResourceStatus.ACTIVE
        )

    private fun createDomainProduct(
        id: Long,
        companyId: Long,
    ) = Product(
        id = id,
        name = "Nome Antigo",
        description = "Descricao Antiga",
        price = BigDecimal("80.0"),
        productionCost = BigDecimal("40.0"),
        sku = "OLD-SKU",
        stockQuantity = 5,
        type = ProductType.PHYSICAL,
        company = mock(Company::class.java).apply { `when`(this.id).thenReturn(companyId) },
        status = ResourceStatus.ACTIVE
    )

    private fun <T> any(type: Class<T>): T = org.mockito.ArgumentMatchers.any(type)
}