package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.CategoryRequestDTO
import br.com.claus.sellvia.application.dto.response.CategoryResponseDTO
import br.com.claus.sellvia.application.usecase.category.CreateCategoryUseCase
import br.com.claus.sellvia.application.usecase.category.DeleteCategoryUseCase
import br.com.claus.sellvia.application.usecase.category.FindPageableCategoryUseCase
import br.com.claus.sellvia.application.usecase.category.UpdateCategoryUseCase
import br.com.claus.sellvia.domain.pagination.CategorySearchQuery
import br.com.claus.sellvia.domain.enums.Direction
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiEndpoints.Category.CATEGORY_ROOT)
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val findByPageableCategoryUseCase: FindPageableCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) {

    @PostMapping
    fun createCategory(
        @RequestBody
        requestDTO: CategoryRequestDTO
    ): ResponseEntity<Any> {
        createCategoryUseCase.execute(requestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getCategories(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(required = false, defaultValue = "id") sortBy: String,
        @RequestParam(required = false, defaultValue = "ASC") sortDirection: String,
        @RequestParam(required = false, defaultValue = "0") companyId: Long,
    ): ResponseEntity<Pagination<CategoryResponseDTO>> {
        val searchQuery = CategorySearchQuery(
            page = page,
            perPage = pageSize,
            terms = "",
            sort = sortBy,
            direction = Direction.valueOf(sortDirection),
            companyId = companyId
        )

        return ResponseEntity.ok()
            .body(findByPageableCategoryUseCase.execute(searchQuery));
    }

    @DeleteMapping("{id}")
    fun deleteCategory(@PathVariable(value = "id") categoryId: Long): ResponseEntity<Any> {
        deleteCategoryUseCase.execute(categoryId)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("{id}")
    fun updateCategory(
        @PathVariable(value = "id") categoryId: Long,
        @RequestBody requestDTO: CategoryRequestDTO
    ): ResponseEntity<CategoryResponseDTO> = ResponseEntity.ok(
        updateCategoryUseCase.execute(categoryId, requestDTO)
    )
}