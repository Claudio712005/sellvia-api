package br.com.claus.sellvia.application.service

import br.com.claus.sellvia.application.port.TokenServicePort
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.InvalidTokenException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException

class PermissionServiceHelper(
    private val tokenService: TokenServicePort
) {

    fun verifyUserCanDoesThisAction(reqCompanyId: Long) {
        val companyId = tokenService.getClaimFromToken("companyId")?.toLong()
        val userRole = tokenService.getClaimFromToken("role")?.let {
            UserRole.valueOf(it.uppercase())
        } ?: throw InvalidTokenException("Token inválido.")

        if(userRole != UserRole.SYSTEM_ADMIN && reqCompanyId != companyId) {
            throw WithoutPermissionException("Usuário sem permissão para manipular/visualizar esse recurso.")
        }
    }
}