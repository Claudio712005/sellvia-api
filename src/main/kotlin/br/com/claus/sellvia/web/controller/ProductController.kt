package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.dto.request.ProductRequestDTO
import br.com.claus.sellvia.application.dto.response.ProductResponseDTO
import br.com.claus.sellvia.application.usecase.product.CreateProductUseCase
import br.com.claus.sellvia.application.usecase.product.DeleteProductUseCase
import br.com.claus.sellvia.application.usecase.product.FindPageableProductUseCase
import br.com.claus.sellvia.application.usecase.product.UpdateProductUseCase
import br.com.claus.sellvia.domain.pagination.Pagination
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(ApiEndpoints.Product.PRODUCT_ROOT)
class ProductController(
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val findPageableProductUseCase: FindPageableProductUseCase,
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestPart("data") request: ProductRequestDTO,
        @RequestPart("image") image: MultipartFile
    ): ResponseEntity<Any> {
        createProductUseCase.execute(
            request = request,
            image = image.bytes
        )

        return ResponseEntity.status(201).build()
    }

    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @RequestBody request: ProductRequestDTO,
        @PathVariable("id") id: Long,
    ): ResponseEntity<ProductResponseDTO> {
        val response = updateProductUseCase.execute(request, id)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        deleteProductUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun findAll(
        filter: ProductSearchQuery
    ): ResponseEntity<Pagination<ProductResponseDTO>>{
        return ResponseEntity.ok(findPageableProductUseCase.execute(filter))
    }
}