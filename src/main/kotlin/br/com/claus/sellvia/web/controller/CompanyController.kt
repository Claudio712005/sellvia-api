package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.CompanyRequestDTO
import br.com.claus.sellvia.application.dto.response.CompanyResponseDTO
import br.com.claus.sellvia.application.usecase.company.UpdateCompanyUseCase
import br.com.claus.sellvia.application.usecase.company.UpdateImageCompanyUseCase
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(ApiEndpoints.Company.COMPANY_ROOT)
class CompanyController(
    private val updateCompanyUseCase: UpdateCompanyUseCase,
    private val updateImageCompanyUseCase: UpdateImageCompanyUseCase,
) {
    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @PathVariable id: Long,
        @RequestBody request: CompanyRequestDTO,
    ): ResponseEntity<CompanyResponseDTO> = ResponseEntity.ok(updateCompanyUseCase.execute(request, id))

    @PutMapping("/{id}${ApiEndpoints.Company.UPDATE_IMAGE}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateImage(
        @PathVariable id: Long,
        @RequestPart("image") image: MultipartFile,
    ): ResponseEntity<CompanyResponseDTO> = ResponseEntity.ok(updateImageCompanyUseCase.execute(id, image.bytes))
}