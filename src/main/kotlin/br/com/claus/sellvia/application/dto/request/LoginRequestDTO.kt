package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.exception.InvalidFieldException

data class LoginRequestDTO(
    val username: String?,
    val password: String?,
) {
    fun validate() {
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            throw InvalidFieldException("Os campos de login do usuário não devem estar vazios.")
        }
    }
}