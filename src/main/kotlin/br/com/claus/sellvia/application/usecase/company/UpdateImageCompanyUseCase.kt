package br.com.claus.sellvia.application.usecase.company

import br.com.claus.sellvia.application.dto.response.CompanyResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.ProcessorResourceStorePort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.FolderDestination
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.repository.CompanyRepository

@UseCase
class UpdateImageCompanyUseCase(
    private val repository: CompanyRepository,
    private val permissionServicePort: PermissionHelperPort,
    private val processorResourceStorePort: ProcessorResourceStorePort,
    private val systemStoragePort: SystemStoragePort,
) {
    fun execute(
        id: Long,
        image: ByteArray,
    ): CompanyResponseDTO {
        val company =
            repository.findById(id)
                ?: throw NotFoundResouceException("Empresa com id '$id' não encontrada.")

        if (!company.isActive) {
            throw NotFoundResouceException("Empresa com id '$id' não encontrada.")
        }

        permissionServicePort.verifyUserCanDoesThisAction(company.id)

        try {
            if (!company.companyUrlLogo.isNullOrBlank()) {
                systemStoragePort.delete(company.companyUrlLogo!!)
            }

            val key =
                processorResourceStorePort.saveOptimizedImage(
                    image,
                    id,
                    FolderDestination.COMPANY,
                    id,
                )

            company.companyUrlLogo = key

            repository.update(
                company
            )
        } catch (e: Exception) {
            throw e
        }

        return company.toResponseDTO()
    }
}