package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidFieldException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserRequestDTOTest {

    @Test
    fun `should validate successfully with correct data`() {
        val dto = createValidDTO()
        dto.validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "null"])
    fun `should throw exception when username is blank`(username: String) {
        val dto = createValidDTO().copy(username = if (username == "null") null else username)
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid-email", "", "test.com"])
    fun `should throw exception when email is invalid`(email: String) {
        val dto = createValidDTO().copy(email = email)
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345", "nospecialchar1A", "NoNumber!", "lowercase1!", "UPPERCASE1!"])
    fun `should throw exception when password is weak`(password: String) {
        val dto = createValidDTO().copy(password = password)
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @Test
    fun `should throw exception when name is blank`() {
        val dto = createValidDTO().copy(name = "")
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @ParameterizedTest
    @ValueSource(strings = ["123", "123456789012", "abcdefghijk"])
    fun `should throw exception when CPF length is not 11`() {
        val dto = createValidDTO().copy(cpf = "")
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @Test
    fun `should throw exception when role is null`() {
        val dto = createValidDTO().copy(userRole = null)
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    @Test
    fun `should throw exception when companyId is missing for non-system admin`() {
        val dto = createValidDTO().copy(userRole = UserRole.COMPANY_USER, companyId = null)
        assertThrows<InvalidFieldException> { dto.validate() }
    }

    private fun createValidDTO() = UserRequestDTO(
        username = "johndoe",
        email = "john@example.com",
        password = "Password123!",
        userRole = UserRole.COMPANY_ADMIN,
        companyId = 1L,
        name = "John Doe",
        cpf = "12345678901"
    )
}