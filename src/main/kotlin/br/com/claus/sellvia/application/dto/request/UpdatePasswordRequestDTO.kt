package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.exception.InvalidFieldException

data class UpdatePasswordRequestDTO(
    val password: String,
    val confirmPassword: String,
    val newPassword: String
) {
    fun validate(): Boolean {

        if (newPassword.isBlank() ||
            !newPassword.matches(
                PASSWORD_REGEX
            )
        ) {
            throw InvalidFieldException(
                "A nova senha deve ter no mínimo 6 caracteres, contendo pelo menos um número, uma letra maiúscula, uma minúscula e um caractere especial."
            )
        }

        if (confirmPassword != password) throw InvalidFieldException("A senha e a confirmação não são iguais.")

        return true
    }
}