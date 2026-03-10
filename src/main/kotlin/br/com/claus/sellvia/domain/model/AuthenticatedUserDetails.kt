package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.UserRole

data class AuthenticatedUserDetails(
    val role: UserRole,
    val companyId: Long?,
)