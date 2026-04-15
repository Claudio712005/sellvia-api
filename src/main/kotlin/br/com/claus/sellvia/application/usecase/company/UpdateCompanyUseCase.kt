package br.com.claus.sellvia.application.usecase.company

import br.com.claus.sellvia.application.dto.request.CompanyRequestDTO
import br.com.claus.sellvia.application.dto.response.CompanyResponseDTO
import br.com.claus.sellvia.application.mapper.toResponseDTO
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.enums.UserRole
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.exception.WithoutPermissionException
import br.com.claus.sellvia.domain.repository.CompanyRepository

@UseCase
class UpdateCompanyUseCase(
    private val repository: CompanyRepository,
    private val permissionHelperPort: PermissionHelperPort,
) {
    fun execute(
        req: CompanyRequestDTO,
        id: Long,
    ): CompanyResponseDTO {
        val user = permissionHelperPort.getDetailsOfAuthenticatedUser()
        if (user.role == UserRole.COMPANY_USER) {
            throw WithoutPermissionException("Usuário sem permissão para atualizar dados da empresa.")
        }

        val entity =
            repository.findById(id)
                ?: throw NotFoundResouceException("Empresa com id '$id' não encontrada.")

        if (!entity.isActive) {
            throw NotFoundResouceException("Empresa com id '$id' não encontrada.")
        }

        permissionHelperPort.verifyUserCanDoesThisAction(entity.id)

        if (repository.existsByMainPhoneNumberAndIdNot(req.mainPhoneNumber, id)) {
            throw IllegalArgumentException("Número de telefone já cadastrado para outra empresa.")
        }

        val entityUpdate =
            entity.copy(
                name = req.name,
                mainPhoneNumber = req.mainPhoneNumber,
                websiteUrl = req.websiteUrl,
                isActive = req.isActive
            )

        return repository.update(entityUpdate).toResponseDTO()
    }
}