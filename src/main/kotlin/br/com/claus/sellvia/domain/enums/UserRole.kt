package br.com.claus.sellvia.domain.enums

enum class UserRole(
    val role: String,
) {

    SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN"),
    COMPANY_ADMIN("ROLE_COMPANY_ADMIN"),
    COMPANY_USER("ROLE_COMPANY_USER"),
}