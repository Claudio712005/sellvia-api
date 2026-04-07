package br.com.claus.sellvia.domain.model

import br.com.claus.sellvia.domain.enums.UserRole

data class AuthenticatedUserDetails(
    val userId : Long ,
    val role: UserRole,
    val companyId: Long?,
)