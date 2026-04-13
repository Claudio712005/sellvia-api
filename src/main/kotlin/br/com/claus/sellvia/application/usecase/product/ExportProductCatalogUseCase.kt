package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.application.port.ImageFetcherPort
import br.com.claus.sellvia.application.port.PdfGeneratorPort
import br.com.claus.sellvia.application.port.PermissionHelperPort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.exception.NotFoundResouceException
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.pagination.CatalogFilterQuery
import br.com.claus.sellvia.domain.pagination.CatalogReportOptions
import br.com.claus.sellvia.domain.pagination.ProductSearchQuery
import br.com.claus.sellvia.domain.repository.CompanyRepository
import br.com.claus.sellvia.domain.repository.ProductRepository

@UseCase
class ExportProductCatalogUseCase(
    private val productRepository: ProductRepository,
    private val companyRepository: CompanyRepository,
    private val pdfGeneratorPort: PdfGeneratorPort,
    private val permissionHelperPort: PermissionHelperPort,
    private val storagePort: SystemStoragePort,
    private val imageFetcherPort: ImageFetcherPort,
) {
    fun execute(
        companyId: Long,
        filter: CatalogFilterQuery,
        options: CatalogReportOptions,
    ): ByteArray {
        permissionHelperPort.verifyUserCanDoesThisAction(companyId)

        val company =
            companyRepository.findById(companyId)
                ?: throw NotFoundResouceException("Empresa com id $companyId não encontrada.")

        val products = fetchAllProducts(companyId, filter)

        val productImages =
            if (options.showProductImages) {
                fetchProductImages(products)
            } else {
                emptyMap()
            }

        val companyLogoBase64 =
            if (options.showCompanyLogo && company.companyUrlLogo.isNotBlank()) {
                imageFetcherPort.fetchAsBase64DataUri(storagePort.buildFileUrl(company.companyUrlLogo))
            } else {
                null
            }

        val html =
            CatalogHtmlBuilder.build(
                company = company,
                products = products,
                productImages = productImages,
                companyLogoBase64 = companyLogoBase64,
                options = options,
            )
        return pdfGeneratorPort.generate(html)
    }

    private fun fetchAllProducts(
        companyId: Long,
        filter: CatalogFilterQuery,
    ): List<Product> {
        val allProducts = mutableListOf<Product>()
        var page = 0
        var totalItems = Long.MAX_VALUE

        while (allProducts.size.toLong() < totalItems) {
            val query =
                ProductSearchQuery(
                    page = page,
                    perPage = 100,
                    sort = filter.sort,
                    direction = filter.direction,
                    companyId = companyId,
                    name = filter.name,
                    categoryId = filter.categoryId,
                    minPrice = filter.minPrice,
                    maxPrice = filter.maxPrice,
                    active = filter.active,
                    sku = filter.sku,
                    type = filter.type,
                )
            val result = productRepository.findAll(query)
            totalItems = result.totalItems
            allProducts.addAll(result.items)
            page++
        }

        return allProducts
    }

    private fun fetchProductImages(products: List<Product>): Map<Long, String> {
        return products
            .filter { it.id != null && !it.imageUrl.isNullOrBlank() }
            .mapNotNull { product ->
                val url = storagePort.buildFileUrl(product.imageUrl)
                val dataUri = imageFetcherPort.fetchAsBase64DataUri(url)
                if (dataUri != null) product.id!! to dataUri else null
            }
            .toMap()
    }
}