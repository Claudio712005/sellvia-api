package br.com.claus.sellvia.web.controller

import br.com.claus.sellvia.application.usecase.product.ExportProductCatalogUseCase
import br.com.claus.sellvia.domain.pagination.CatalogFilterQuery
import br.com.claus.sellvia.domain.pagination.CatalogReportOptions
import br.com.claus.sellvia.infrastructure.config.ApiEndpoints
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping(ApiEndpoints.Company.COMPANY_ROOT)
class ProductCatalogController(
    private val exportProductCatalogUseCase: ExportProductCatalogUseCase,
) {
    @GetMapping("/{companyId}${ApiEndpoints.Company.CATALOG_PDF}")
    fun exportCatalogPdf(
        @PathVariable companyId: Long,
        filter: CatalogFilterQuery,
        options: CatalogReportOptions,
    ): ResponseEntity<ByteArray> {
        val pdfBytes = exportProductCatalogUseCase.execute(companyId, filter, options)

        val filename = "catalogo-$companyId-${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)}.pdf"

        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_PDF
                contentDisposition = ContentDisposition.attachment().filename(filename).build()
                contentLength = pdfBytes.size.toLong()
            }

        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes)
    }
}