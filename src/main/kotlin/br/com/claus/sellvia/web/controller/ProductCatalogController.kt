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
    /**
     * Gera e faz download do catálogo de produtos em PDF para a empresa informada.
     *
     * GET /api/v1.0.0/companies/{companyId}/catalog/pdf
     *
     * ── Filtros (todos opcionais) ──────────────────────────────────────────
     *   name          filtra por nome (parcial, case-insensitive)
     *   categoryId    filtra por categoria
     *   minPrice      preço mínimo
     *   maxPrice      preço máximo
     *   active        true = apenas ativos | false = apenas inativos
     *   sku           SKU exato
     *   type          PHYSICAL | DIGITAL | SERVICE
     *   sort          campo de ordenação (padrão: name)
     *   direction     ASC | DESC (padrão: ASC)
     *
     * ── Opções de exibição (todos opcionais) ──────────────────────────────
     *   showProductImages       exibe imagem de cada produto       (padrão: true)
     *   showCompanyLogo         exibe logo da empresa no cabeçalho (padrão: true)
     *   showCompanyBusinessName exibe razão social                 (padrão: true)
     *   showCompanyCnpj         exibe CNPJ                         (padrão: true)
     *   showCompanyWebsite      exibe site da empresa              (padrão: true)
     *   showStats               exibe painel de estatísticas       (padrão: true)
     *   showAveragePrice        exibe preço médio nas estatísticas  (padrão: true)
     *   showActiveCount         exibe contagem de ativos           (padrão: true)
     *   showTypeBreakdown       exibe contagem por tipo            (padrão: true)
     *   showSku                 exibe SKU no card do produto       (padrão: true)
     *   showStock               exibe estoque (produtos físicos)   (padrão: true)
     *   showProductionCost      exibe custo de produção            (padrão: false)
     *   showCategory            exibe categoria no card            (padrão: true)
     *   showDescription         exibe descrição no card            (padrão: true)
     *   showStatus              exibe badge ativo/inativo          (padrão: true)
     */
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