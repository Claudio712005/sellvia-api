package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.repository.ProductRepository
import io.kotest.assertions.any
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProductUseCaseTest {

    private val repository = mockk<ProductRepository>()
    private val permissionHelperPort = mockk<PermissionHelperPort>()

    private lateinit var useCase: DeleteProductUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteProductUseCase(
            repository = repository,
            permissionHelperPort = permissionHelperPort
        )
    }

    @Test
    fun `should throw exception when product ID is invalid`() {
        assertThrows<IllegalArgumentException> { useCase.execute(-1L) }
    }

    @Test
    fun `should throw exception when product not found`() {
        every { repository.findById(any()) } returns null

        assertThrows<IllegalArgumentException> { useCase.execute(1L) }
    }

    @Test
    fun `should delete product successfully when data is valid`() {
        val product = mockk<Product> {
            every { company.id } returns 1L
        }

        every { repository.findById(any()) } returns product
        every { permissionHelperPort.verifyUserCanDoesThisAction(any()) } just runs

        every { repository.delete(any()) } just runs

        useCase.execute(1L)

        verify(exactly = 1) { repository.findById(1L) }
        verify(exactly = 1) { permissionHelperPort.verifyUserCanDoesThisAction(1L) }
        verify(exactly = 1) { repository.delete(1L) }
    }
}