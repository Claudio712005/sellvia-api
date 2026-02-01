package br.com.claus.sellvia.infrastructure.config

import br.com.claus.sellvia.domain.enums.UserRole

object ApiEndpoints {
    val PUBLIC_PATHS: Array<String> = arrayOf(
        "/auth/**",
        "/h2-console/**",
        "/swagger-ui/**",
        "/error"
    )

    val ROLE_PERMISSIONS: Map<UserRole, Array<String>> = mapOf(
        UserRole.SYSTEM_ADMIN to emptyArray<String>(),
        UserRole.COMPANY_ADMIN to emptyArray<String>(),
        UserRole.COMPANY_USER to emptyArray<String>(),
    )
}