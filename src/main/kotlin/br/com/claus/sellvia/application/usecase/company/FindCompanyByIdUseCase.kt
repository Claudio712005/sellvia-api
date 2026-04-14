package br.com.claus.sellvia.application.usecase.company

import br.com.claus.sellvia.application.dto.response.CompanyResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.CompanyRepository

@UseCase
class FindCompanyByIdUseCase(
    private val repository: CompanyRepository,
    private val permissionHelperPort: PermissionHelperPort,
    private val storagePort: SystemStoragePort,
) {
    fun execute(id: Long): CompanyResponseDTO {
        val company =
            repository.findById(id)
                ?: throw NotFoundResouceException("Empresa com ID $id não encontrada.")

        if (!company.isActive) throw NotFoundResouceException("Empresa com ID $id não encontrada.")

        permissionHelperPort.verifyUserCanDoesThisAction(company.id)

        return company.toResponseDTO().copy(
            companyUrlLogo = storagePort.buildFileUrl(company.companyUrlLogo)
        )
    }
}