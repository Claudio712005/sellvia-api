package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.usecase.category.CreateCategoryUseCase
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiEndpoints.Category.CATEGORY_ROOT)
class CategoryController(
    private val categoryUseCase: CreateCategoryUseCase
) {

    @PostMapping
    fun createCategory(
        @RequestBody
        requestDTO: CategoryRequestDTO
    ): ResponseEntity<Any> {
        categoryUseCase.execute(requestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}