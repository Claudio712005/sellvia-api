package br.com.claus.sellvia.application.port

interface PermissionHelperPort {

    fun verifyUserCanDoesThisAction(reqCompanyId: Long)
}