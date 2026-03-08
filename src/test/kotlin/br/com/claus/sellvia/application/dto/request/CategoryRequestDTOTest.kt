package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.exception.InvalidFieldException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CategoryRequestDTOTest {
    @Test
    fun `should not throw exception when dto is valid`() {
        val dto = CategoryRequestDTO(name = "Category", description = "Valid description", companyId = 1L)

        assertDoesNotThrow { dto.validate() }
    }

    @Test
    fun `should throw exception when name is null`() {
        val dto =
            CategoryRequestDTO(
                name = null,
                description = "Valid description",
                companyId = 1L
            )

        val exception = assertThrows(InvalidFieldException::class.java) { dto.validate() }

        assertEquals("O nome deve ser preenchido.", exception.message)
    }

    @Test
    fun `should throw exception when name is blank`() {
        val dto =
            CategoryRequestDTO(
                name = "   ",
                description = "Valid description",
                companyId = 1L
            )

        val exception =
            assertThrows(InvalidFieldException::class.java) {
                dto.validate()
            }

        assertEquals("O nome deve ser preenchido.", exception.message)
    }

    @Test
    fun `should throw exception when description is null`() {
        val dto =
            CategoryRequestDTO(
                name = "Category",
                description = null,
                companyId = 1L
            )

        val exception =
            assertThrows(InvalidFieldException::class.java) {
                dto.validate()
            }

        assertEquals("A descrição deve ser preenchida.", exception.message)
    }

    @Test
    fun `should throw exception when description is blank`() {
        val dto =
            CategoryRequestDTO(
                name = "Category",
                description = "",
                companyId = 1L
            )

        val exception =
            assertThrows(InvalidFieldException::class.java) {
                dto.validate()
            }

        assertEquals("A descrição deve ser preenchida.", exception.message)
    }

    @Test
    fun `should throw exception when companyId is null`() {
        val dto =
            CategoryRequestDTO(
                name = "Category",
                description = "Valid description",
                companyId = null
            )

        val exception =
            assertThrows(InvalidFieldException::class.java) {
                dto.validate()
            }

        assertEquals("A empresa deve ser informada.", exception.message)
    }
}