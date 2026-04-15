package br.com.claus.sellvia.infrastructure.adapter

import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.model.AuthenticatedUserDetails
import org.springframework.stereotype.Component

@Component
class PermissionHelperAdapter(
    private val tokenService: TokenServicePort,
) : PermissionHelperPort {
    override fun verifyUserCanDoesThisAction(reqCompanyId: Long?) {
        val companyId = tokenService.getClaimFromToken("companyId")?.toLong()
        val userRole =
            tokenService.getClaimFromToken("role")?.let {
                UserRole.valueOf(it.uppercase())
            } ?: throw InvalidTokenException("Token inválido.")

        if (userRole != UserRole.SYSTEM_ADMIN && reqCompanyId != companyId) {
            throw WithoutPermissionException("Usuário sem permissão para manipular/visualizar esse recurso.")
        }
    }

    override fun getDetailsOfAuthenticatedUser(): AuthenticatedUserDetails {
        val companyId = tokenService.getClaimFromToken("companyId")?.toLong()
        val userRole =
            tokenService.getClaimFromToken("role")?.let {
                UserRole.valueOf(it.uppercase())
            } ?: throw InvalidTokenException("Token inválido.")
        val userId = tokenService.getClaimFromToken("userId")?.toLong() ?: throw InvalidTokenException("Token inválido.")

        return AuthenticatedUserDetails(
            userId = userId,
            role = userRole,
            companyId = companyId
        )
    }
}