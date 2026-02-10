package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.exception.InvalidFieldException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class LoginRequestDTOTest {

    @Test
    fun `should not throw exception when username and password are valid`() {
        val dto = LoginRequestDTO(
            username = "user_test",
            password = "password123"
        )

        assertDoesNotThrow {
            dto.validate()
        }
    }

    @Test
    fun `should throw exception when username is null`() {
        val dto = LoginRequestDTO(
            username = null,
            password = "password123"
        )

        val exception = assertThrows(InvalidFieldException::class.java) {
            dto.validate()
        }

        assertEquals(
            "Os campos de login do usuário não devem estar vazios.",
            exception.message
        )
    }

    @Test
    fun `should throw exception when password is null`() {
        val dto = LoginRequestDTO(
            username = "user_test",
            password = null
        )

        val exception = assertThrows(InvalidFieldException::class.java) {
            dto.validate()
        }

        assertEquals(
            "Os campos de login do usuário não devem estar vazios.",
            exception.message
        )
    }

    @Test
    fun `should throw exception when username is blank`() {
        val dto = LoginRequestDTO(
            username = "   ",
            password = "password123"
        )

        val exception = assertThrows(InvalidFieldException::class.java) {
            dto.validate()
        }

        assertEquals(
            "Os campos de login do usuário não devem estar vazios.",
            exception.message
        )
    }

    @Test
    fun `should throw exception when password is blank`() {
        val dto = LoginRequestDTO(
            username = "user_test",
            password = "   "
        )

        val exception = assertThrows(InvalidFieldException::class.java) {
            dto.validate()
        }

        assertEquals(
            "Os campos de login do usuário não devem estar vazios.",
            exception.message
        )
    }
}
