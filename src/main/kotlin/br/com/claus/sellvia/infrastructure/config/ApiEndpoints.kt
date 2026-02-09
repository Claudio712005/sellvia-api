package br.com.claus.sellvia.infrastructure.config

import br.com.claus.sellvia.domain.enums.UserRole

object ApiEndpoints {

    private const val PREFIX = "/api"
    private const val VERSION = "/v1.0.0"
    const val API_ROOT = "$PREFIX$VERSION"

    object System {
        const val SWAGGER_ROOT = "/swagger-ui/**"
        const val DOCS_ROOT = "/v3/api-docs/**"
        const val H2_CONSOLE_ROOT = "/h2-console/**"
        const val ERROR_ROOT = "/error"
    }

    object Auth {
        const val AUTH_ROOT = "$API_ROOT/auth"

        const val LOGIN = "/login"
        const val REGISTRY = "/registry"
        const val REFRESH_TOKEN = "/refresh-token"

        const val LOGIN_ROOT = "$AUTH_ROOT$LOGIN"
        const val REGISTRY_ROOT = "$AUTH_ROOT$REGISTRY"
        const val REFRESH_TOKEN_ROOT = "$AUTH_ROOT$REFRESH_TOKEN"
    }

    val PUBLIC = arrayOf(
        System.H2_CONSOLE_ROOT,
        System.ERROR_ROOT,
        System.SWAGGER_ROOT,
        System.DOCS_ROOT,
        Auth.LOGIN_ROOT,
        Auth.REFRESH_TOKEN_ROOT
    )

    val PRIVATE: Map<String, Array<UserRole>> = mapOf(
        Auth.REGISTRY_ROOT to arrayOf(UserRole.SYSTEM_ADMIN, UserRole.COMPANY_ADMIN)
    )
}