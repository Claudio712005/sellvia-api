package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidFieldException

private val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$".toRegex()

data class UserRequestDTO(
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val userRole: UserRole? = null,
    val companyId: Long? = null,
    val name: String? = null,
    val cpf: String? = null,
) {

    fun validate() {
        if (username.isNullOrBlank()) throw InvalidFieldException("Username não pode ser vazio")

        if (email.isNullOrBlank() || !email.contains("@")) throw InvalidFieldException("Email inválido ou vazio")

        if (password.isNullOrBlank() || !password.matches(PASSWORD_REGEX)) throw InvalidFieldException("A senha deve ter no mínimo 6 caracteres, contendo pelo menos um número, uma letra maiúscula, uma minúscula e um caractere especial.")

        if (name.isNullOrBlank()) throw InvalidFieldException("Nome é obrigatório")

        if (cpf.isNullOrBlank() || cpf.length != 11) throw InvalidFieldException("CPF deve conter 11 dígitos")

        if (userRole == null) throw InvalidFieldException("Role do usuário é obrigatória")

        if (userRole != UserRole.SYSTEM_ADMIN && companyId == null) throw InvalidFieldException("A empresa deve ser informada")
    }
}