package br.com.claus.sellvia.application.port

import br.com.claus.sellvia.domain.model.AuthenticatedUserDetails

interface PermissionHelperPort {
    fun verifyUserCanDoesThisAction(reqCompanyId: Long?)

    fun getDetailsOfAuthenticatedUser(): AuthenticatedUserDetails
}