package br.com.claus.sellvia.application.usecase.product

import br.com.claus.sellvia.domain.enums.CardStyle
import br.com.claus.sellvia.domain.enums.ProductType
import br.com.claus.sellvia.domain.enums.ResourceStatus
import br.com.claus.sellvia.domain.model.Company
import br.com.claus.sellvia.domain.model.Product
import br.com.claus.sellvia.domain.pagination.CatalogReportOptions
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CatalogHtmlBuilder {
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")

    private data class CardSizing(
        val imageHeight: Int,
        val cellWidthPct: Int,
        val cellPaddingPx: Int,
        val nameFontSize: Int,
        val priceFontSize: Int,
        val bodyFontSize: Int,
        val badgeFontSize: Int,
        val headerPadding: String,
        val bodyPadding: String,
        val rowGapPx: Int,
    )

    private fun sizingFor(columns: Int) =
        when (columns) {
            1 -> CardSizing(220, 98, 0, 16, 22, 12, 10, "14px 20px", "16px 20px", 10)
            2 -> CardSizing(130, 49, 6, 12, 17, 10, 9, "9px 13px", "11px 13px", 8)
            3 -> CardSizing(100, 32, 4, 11, 14, 9, 8, "7px 10px", "9px 10px", 6)
            4 -> CardSizing(75, 24, 3, 10, 12, 9, 8, "5px 8px", "7px 8px", 5)
            else -> CardSizing(130, 49, 6, 12, 17, 10, 9, "9px 13px", "11px 13px", 8)
        }

    fun build(
        company: Company,
        products: List<Product>,
        productImages: Map<Long, String>,
        companyLogoBase64: String?,
        options: CatalogReportOptions,
    ): String {
        val cols = options.effectiveColumns
        val sizing = sizingFor(cols)
        val now = LocalDateTime.now().format(DATE_FORMATTER)

        val activeCount = products.count { it.status == ResourceStatus.ACTIVE }
        val physicalCount = products.count { it.type == ProductType.PHYSICAL }
        val digitalCount = products.count { it.type == ProductType.DIGITAL }
        val serviceCount = products.count { it.type == ProductType.SERVICE }
        val avgPrice = avgPrice(products)

        return buildString {
            append(htmlOpen(company, sizing, options))
            append(pageHeader(company, companyLogoBase64, now, products.size, options))
            if (options.showStats) {
                append(
                    statsSection(
                        products.size,
                        activeCount,
                        physicalCount,
                        digitalCount,
                        serviceCount,
                        avgPrice,
                        options,
                    ),
                )
            }
            append(productsSection(products, productImages, options, sizing, cols))
            append(htmlClose(company, now, products.size))
        }
    }

    private fun htmlOpen(
        company: Company,
        sizing: CardSizing,
        options: CatalogReportOptions,
    ) = """
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <title>Catálogo de Produtos — ${escape(company.name)}</title>
            <style type="text/css">
                ${css(sizing, options)}
            </style>
        </head>
        <body>
        """.trimIndent()

    private fun htmlClose(
        company: Company,
        generatedAt: String,
        total: Int,
    ) = """
        <div class="page-footer">
            <table class="footer-table">
                <tr>
                    <td class="footer-left">
                        <p class="footer-brand">${escape(company.name)}</p>
                        <p class="footer-tagline">Catálogo gerado pelo Sellvia</p>
                    </td>
                    <td class="footer-right">
                        <p class="footer-meta">$total produto(s)&#160;&#160;|&#160;&#160;$generatedAt</p>
                    </td>
                </tr>
            </table>
        </div>
        </body>
        </html>
        """.trimIndent()

    private fun css(
        sizing: CardSizing,
        options: CatalogReportOptions,
    ): String {
        val isEmployee = options.cardStyle == CardStyle.EMPLOYEE

        val cardBg = if (isEmployee) "#fafafa" else "#ffffff"
        val bodyBg = if (isEmployee) "#f1f5f9" else "#f0f4f8"

        return """
            @page { size: A4; margin: 0; }
            body {
                font-family: Arial, Helvetica, sans-serif;
                margin: 0;
                padding: 0 0 52px 0;
                background-color: $bodyBg;
                color: #2d3748;
            }
            /* ── Page header ── */
            .page-header { background-color: #1a2942; padding: 28px 36px 24px 36px; }
            .header-layout { width: 100%; }
            .header-left  { vertical-align: middle; width: 62%; }
            .header-right { vertical-align: middle; text-align: right; width: 38%; }
            .company-logo { width: 72px; height: 72px; margin-bottom: 8px; display: block; }
            .company-name { font-size: 24px; font-weight: bold; color: #fff; margin: 0 0 3px 0; padding: 0; }
            .company-sub  { font-size: 12px; color: #94a3b8; margin: 0 0 2px 0; padding: 0; }
            .company-meta { font-size: 11px; color: #64748b; margin: 2px 0; padding: 0; }
            .catalog-label { font-size: 10px; font-weight: bold; color: #3b82f6; letter-spacing: 2px; margin: 12px 0 0 0; padding: 0; }
            .gen-label { font-size: 9px; color: #64748b; margin: 0 0 2px 0; padding: 0; text-transform: uppercase; letter-spacing: 1px; }
            .gen-date  { font-size: 11px; color: #94a3b8; font-weight: bold; margin: 0 0 14px 0; padding: 0; }
            .total-label  { font-size: 9px; color: #64748b; margin: 0 0 2px 0; padding: 0; text-transform: uppercase; letter-spacing: 1px; }
            .total-count  { font-size: 40px; font-weight: bold; color: #3b82f6; margin: 0; padding: 0; }
            .total-suffix { font-size: 11px; color: #64748b; margin: 0; padding: 0; }
            /* ── Accent bar ── */
            .accent-bar { height: 4px; background-color: #3b82f6; }
            /* ── Stats panel ── */
            .stats-wrapper { background-color: #1e3354; padding: 16px 36px; }
            .stats-table { width: 100%; }
            .stat-cell  { text-align: center; vertical-align: middle; padding: 6px 4px; }
            .stat-sep   { width: 1px; background-color: #2d4a72; vertical-align: middle; }
            .stat-value { font-size: 20px; font-weight: bold; color: #fff; margin: 0; padding: 0; }
            .stat-label { font-size: 9px; color: #94a3b8; margin: 3px 0 0 0; padding: 0; text-transform: uppercase; letter-spacing: 1px; }
            /* ── Section title ── */
            .section-wrapper { padding: 18px 36px 8px 36px; }
            .section-title { font-size: 13px; font-weight: bold; color: #1a2942; margin: 0 0 3px 0; padding: 0 0 7px 0; border-bottom: 2px solid #3b82f6; }
            /* ── Products grid ── */
            .products-wrapper { padding: 0 36px 24px 36px; }
            .product-grid { width: 100%; border-collapse: collapse; }
            .product-cell { width: ${sizing.cellWidthPct}%; vertical-align: top; padding: ${sizing.cellPaddingPx}px; }
            /* ── page-break ── */
            tr.product-pair { page-break-inside: avoid; }
            /* ── Card shared ── */
            .product-card {
                background-color: $cardBg;
                border: 1px solid #e2e8f0;
                page-break-inside: avoid;
            }
            /* ── Card image ── */
            .card-img { width: 100%; height: ${sizing.imageHeight}px; display: block; }
            .card-no-img {
                width: 100%;
                height: ${sizing.imageHeight}px;
                background-color: #e8edf5;
                text-align: center;
                vertical-align: middle;
                display: table-cell;
                font-size: ${sizing.bodyFontSize}px;
                color: #94a3b8;
            }
            /* ── CUSTOMER card (design atrativo) ── */
            .cust-accent   { height: 5px; background-color: #3b82f6; }
            .cust-tag-bar  { background-color: #f8fafc; padding: 5px 10px; border-bottom: 1px solid #e5e7eb; }
            .cust-tag-tbl  { width: 100%; }
            .cust-tag-l    { vertical-align: middle; }
            .cust-tag-r    { vertical-align: middle; text-align: right; }
            .cust-content  { padding: ${sizing.bodyPadding}; }
            .cust-name     { font-size: ${sizing.nameFontSize}px; font-weight: bold; color: #111827; margin: 0 0 2px 0; padding: 0; }
            .cust-sku      { font-size: ${sizing.badgeFontSize}px; color: #9ca3af; margin: 0 0 6px 0; padding: 0; }
            .cust-price-box {
                background-color: #f0fdf4;
                border-left: 3px solid #16a34a;
                padding: 6px 10px;
                margin: 8px 0 6px 0;
            }
            .cust-price-lbl { font-size: ${sizing.badgeFontSize}px; color: #15803d; text-transform: uppercase; letter-spacing: 1px; margin: 0; padding: 0; }
            .cust-price-val { font-size: ${sizing.priceFontSize}px; font-weight: bold; color: #15803d; margin: 2px 0 0 0; padding: 0; }
            .cust-cost      { font-size: ${sizing.bodyFontSize}px; color: #9ca3af; margin: 0 0 4px 0; padding: 0; }
            /* ── EMPLOYEE card ── */
            .emp-header  { background-color: #374151; padding: ${sizing.headerPadding}; }
            .emp-info-bar { width: 100%; margin-bottom: 4px; }
            .emp-type-cell   { vertical-align: middle; }
            .emp-status-cell { vertical-align: middle; text-align: right; }
            .emp-name { font-size: ${sizing.nameFontSize}px; font-weight: bold; color: #f1f5f9; margin: 4px 0 0 0; padding: 0; }
            .emp-sku  { font-size: ${sizing.badgeFontSize}px; color: #64748b; margin: 2px 0 0 0; padding: 0; }
            .emp-body { padding: ${sizing.bodyPadding}; }
            .emp-price-block { background-color: #f0fdf4; padding: 6px 8px; margin-bottom: 6px; }
            .emp-price-row   { width: 100%; }
            .emp-price-lbl { font-size: ${sizing.badgeFontSize}px; font-weight: bold; color: #15803d; margin: 0; padding: 0; }
            .emp-price-val { font-size: ${sizing.priceFontSize}px; font-weight: bold; color: #15803d; margin: 0; padding: 0; text-align: right; }
            .emp-cost-lbl  { font-size: ${sizing.badgeFontSize}px; font-weight: bold; color: #9f1239; margin: 2px 0 0 0; padding: 0; }
            .emp-cost-val  { font-size: ${sizing.bodyFontSize}px; color: #9f1239; margin: 0; padding: 0; text-align: right; }
            .emp-margin-lbl { font-size: ${sizing.badgeFontSize}px; font-weight: bold; color: #1d4ed8; margin: 2px 0 0 0; padding: 0; }
            .emp-margin-val { font-size: ${sizing.bodyFontSize}px; font-weight: bold; color: #1d4ed8; margin: 0; padding: 0; text-align: right; }
            /* ── Shared detail rows ── */
            .divider { border-top: 1px solid #e5e7eb; margin: 6px 0; }
            .detail-table { width: 100%; }
            .detail-lbl { font-size: ${sizing.bodyFontSize}px; font-weight: bold; color: #6b7280; width: 40%; vertical-align: top; padding: 1px 0; }
            .detail-val { font-size: ${sizing.bodyFontSize}px; color: #374151; vertical-align: top; padding: 1px 0; }
            .desc-block {
                font-size: ${sizing.bodyFontSize}px; color: #6b7280; font-style: italic;
                margin: 6px 0 0 0; padding: 4px 6px;
                background-color: #f8fafc; border-left: 2px solid #3b82f6;
            }
            /* ── Badges ── */
            .badge { font-size: ${sizing.badgeFontSize}px; font-weight: bold; padding: 2px 5px; text-transform: uppercase; letter-spacing: 1px; }
            .badge-type-phys { background-color: #dbeafe; color: #1e40af; }
            .badge-type-dig  { background-color: #ede9fe; color: #5b21b6; }
            .badge-type-svc  { background-color: #fef9c3; color: #854d0e; }
            .badge-active    { background-color: #dcfce7; color: #15803d; }
            .badge-inactive  { background-color: #fee2e2; color: #b91c1c; }
            /* ── Placeholder ── */
            .placeholder { background-color: #f8fafc; border: 1px dashed #e2e8f0; }
            /* ── Footer ── */
            .page-footer {
                position: fixed; bottom: 0; left: 0; right: 0;
                height: 44px; background-color: #1a2942; padding: 0 36px;
            }
            .footer-table { width: 100%; height: 44px; }
            .footer-left  { vertical-align: middle; }
            .footer-right { vertical-align: middle; text-align: right; }
            .footer-brand   { font-size: 12px; font-weight: bold; color: #3b82f6; margin: 0; padding: 0; }
            .footer-tagline { font-size: 9px; color: #64748b; margin: 2px 0 0 0; padding: 0; }
            .footer-meta    { font-size: 9px; color: #64748b; margin: 0; padding: 0; }
            """.trimIndent()
    }

    private fun pageHeader(
        company: Company,
        logoBase64: String?,
        generatedAt: String,
        total: Int,
        options: CatalogReportOptions,
    ): String {
        val logoHtml =
            if (options.showCompanyLogo && logoBase64 != null) {
                """<img src="$logoBase64" class="company-logo" alt="Logo"/>"""
            } else {
                ""
            }
        val businessNameHtml =
            if (options.showCompanyBusinessName && company.businessName.isNotBlank()) {
                """<p class="company-sub">${escape(company.businessName)}</p>"""
            } else {
                ""
            }
        val cnpjHtml =
            if (options.showCompanyCnpj && company.cnpj.isNotBlank()) {
                """<p class="company-meta">CNPJ: ${escape(company.cnpj)}</p>"""
            } else {
                ""
            }
        val websiteHtml =
            if (options.showCompanyWebsite && company.websiteUrl.isNotBlank()) {
                """<p class="company-meta">${escape(company.websiteUrl)}</p>"""
            } else {
                ""
            }
        return """
            <div class="page-header">
                <table class="header-layout"><tr>
                    <td class="header-left">
                        $logoHtml
                        <p class="company-name">${escape(company.name)}</p>
                        $businessNameHtml
                        $cnpjHtml
                        $websiteHtml
                    <p class="catalog-label">&#9679; Catálogo Oficial de Produtos</p>
                    </td>
                    <td class="header-right">
                    <p class="gen-label">Gerado em</p>
                    <p class="gen-date">$generatedAt</p>
                    <p class="total-label">Total de produtos</p>
                    <p class="total-count">$total</p>
                    <p class="total-suffix">produto(s)</p>
                    </td>
                    </tr></table>
                    </div>
                    <div class="accent-bar">&#160;</div>
            """.trimIndent()
    }

    private fun statsSection(
        total: Int,
        active: Int,
        physical: Int,
        digital: Int,
        service: Int,
        avgPrice: BigDecimal,
        options: CatalogReportOptions,
    ): String {
        val cells =
            buildString {
                append(statCell(total.toString(), "Total"))
                if (options.showActiveCount) {
                    append(statSep())
                    append(statCell(active.toString(), "Ativos"))
                }
                if (options.showTypeBreakdown) {
                    append(statSep())
                    append(statCell(physical.toString(), "Físicos"))
                    append(statSep())
                    append(statCell(digital.toString(), "Digitais"))
                    if (service > 0) {
                        append(statSep())
                        append(statCell(service.toString(), "Serviços"))
                    }
                }
                if (options.showAveragePrice) {
                    append(statSep())
                    append(statCell("R$&#160;${fmt(avgPrice)}", "Preço Médio"))
                }
            }
        return """
            <div class="stats-wrapper">
                <table class="stats-table"><tr>$cells</tr></table>
            </div>
            """.trimIndent()
    }

    private fun statCell(
        v: String,
        l: String,
    ) = """<td class="stat-cell"><p class="stat-value">$v</p><p class="stat-label">$l</p></td>"""

    private fun statSep() = """<td class="stat-sep">&#160;</td>"""

    private fun productsSection(
        products: List<Product>,
        images: Map<Long, String>,
        options: CatalogReportOptions,
        sizing: CardSizing,
        cols: Int,
    ): String {
        if (products.isEmpty()) {
            return """
                <div class="section-wrapper"><p class="section-title">Produtos</p></div>
                <div class="products-wrapper">
                    <p style="text-align:center;color:#94a3b8;font-size:13px;padding:40px 0;">
                        Nenhum produto encontrado com os filtros aplicados.
                    </p>
                </div>
                """.trimIndent()
        }

        val rows =
            buildString {
                products.chunked(cols).forEach { group ->
                    append("""<tr class="product-pair">""")
                    group.forEach { p ->
                        append("""<td class="product-cell">""")
                        append(productCard(p, images, options, sizing))
                        append("""</td>""")
                    }
                    repeat(cols - group.size) {
                        append("""<td class="product-cell"><div class="placeholder">&#160;</div></td>""")
                    }
                    append("""</tr>""")
                    append("""<tr><td colspan="$cols" style="height:${sizing.rowGapPx}px;">&#160;</td></tr>""")
                }
            }

        return """
            <div class="section-wrapper">
                <p class="section-title">Produtos (${products.size}) — $cols por linha</p>
            </div>
            <div class="products-wrapper">
                <table class="product-grid">$rows</table>
            </div>
            """.trimIndent()
    }

    private fun productCard(
        product: Product,
        images: Map<Long, String>,
        options: CatalogReportOptions,
        sizing: CardSizing,
    ) = when (options.cardStyle) {
        CardStyle.CUSTOMER -> customerCard(product, images, options, sizing)
        CardStyle.EMPLOYEE -> employeeCard(product, images, options, sizing)
    }

    private fun customerCard(
        product: Product,
        images: Map<Long, String>,
        options: CatalogReportOptions,
        sizing: CardSizing,
    ): String {
        val imageBlock = imageBlock(product, images, options.showProductImages, sizing)
        val typeBadgeClass = typeBadgeClass(product.type)
        val typeLabel = typeLabel(product.type)
        val (statusClass, statusLabel) = statusBadge(product.status)

        val tagBar =
            buildString {
                append("""<div class="cust-tag-bar"><table class="cust-tag-tbl"><tr>""")
                if (options.showStatus) {
                    append("""<td class="cust-tag-l"><span class="badge $statusClass">$statusLabel</span></td>""")
                } else {
                    append("""<td class="cust-tag-l">&#160;</td>""")
                }
                append("""<td class="cust-tag-r"><span class="badge $typeBadgeClass">$typeLabel</span></td>""")
                append("""</tr></table></div>""")
            }

        val costLine =
            if (options.showProductionCost) {
                """<p class="cust-cost">Custo de prod.: R$&#160;${fmt(product.productionCost)}</p>"""
            } else {
                ""
            }

        val detailRows =
            buildString {
                if (options.showStock && product.type == ProductType.PHYSICAL) {
                    append(detailRow("Estoque:", "${product.stockQuantity ?: "—"} unid."))
                }
                product.category?.takeIf { options.showCategory }?.let {
                    append(detailRow("Categoria:", escape(it.name!!)))
                }
            }

        val descBlock = descriptionBlock(product, options.showDescription)

        return """
            <div class="product-card">
                <div class="cust-accent">&#160;</div>
                $imageBlock
                $tagBar
                <div class="cust-content">
                    <p class="cust-name">${escape(product.name)}</p>
                    ${if (options.showSku) """<p class="cust-sku">SKU: ${escape(product.sku)}</p>""" else ""}
                    <div class="cust-price-box">
                        <p class="cust-price-lbl">Preço de Venda</p>
                        <p class="cust-price-val">R$&#160;${fmt(product.price)}</p>
                    </div>
                    $costLine
                    ${if (detailRows.isNotBlank()) """<div class="divider">&#160;</div><table class="detail-table">$detailRows</table>""" else ""}
                    $descBlock
                </div>
            </div>
            """.trimIndent()
    }

    private fun employeeCard(
        product: Product,
        images: Map<Long, String>,
        options: CatalogReportOptions,
        sizing: CardSizing,
    ): String {
        val imageBlock = imageBlock(product, images, options.showProductImages, sizing)
        val typeBadgeClass = typeBadgeClass(product.type)
        val typeLabel = typeLabel(product.type)
        val (statusClass, statusLabel) = statusBadge(product.status)

        val marginBlock =
            if (options.showProductionCost && product.price > BigDecimal.ZERO) {
                val pct = marginPct(product.price, product.productionCost)
                """
                <tr>
                    <td><p class="emp-margin-lbl">Margem:</p></td>
                    <td><p class="emp-margin-val">$pct</p></td>
                </tr>
                """.trimIndent()
            } else {
                ""
            }

        val costBlock =
            if (options.showProductionCost) {
                """
                <tr>
                    <td><p class="emp-cost-lbl">Custo:</p></td>
                    <td><p class="emp-cost-val">R$&#160;${fmt(product.productionCost)}</p></td>
                </tr>
                $marginBlock
                """.trimIndent()
            } else {
                ""
            }

        val detailRows =
            buildString {
                if (options.showStock && product.type == ProductType.PHYSICAL) {
                    append(detailRow("Estoque:", "${product.stockQuantity ?: "—"} unid."))
                }
                product.category?.takeIf { options.showCategory }?.let {
                    append(detailRow("Categoria:", escape(it.name!!)))
                }
            }

        val descBlock = descriptionBlock(product, options.showDescription)

        return """
            <div class="product-card">
                $imageBlock
                <div class="emp-header">
                    <table class="emp-info-bar"><tr>
                        <td class="emp-type-cell">
                            <span class="badge $typeBadgeClass">$typeLabel</span>
                        </td>
                        ${if (options.showStatus) """<td class="emp-status-cell"><span class="badge $statusClass">$statusLabel</span></td>""" else ""}
                    </tr></table>
                    <p class="emp-name">${escape(product.name)}</p>
                    ${if (options.showSku) """<p class="emp-sku">SKU: ${escape(product.sku)}</p>""" else ""}
                </div>
                <div class="emp-body">
                    <div class="emp-price-block">
                        <table class="emp-price-row"><tr>
                            <td><p class="emp-price-lbl">Preço:</p></td>
                            <td><p class="emp-price-val">R$&#160;${fmt(product.price)}</p></td>
                        </tr>
                        $costBlock
                        </table>
                    </div>
                    ${if (detailRows.isNotBlank()) """<div class="divider">&#160;</div><table class="detail-table">$detailRows</table>""" else ""}
                    $descBlock
                </div>
            </div>
            """.trimIndent()
    }

    private fun imageBlock(
        product: Product,
        images: Map<Long, String>,
        show: Boolean,
        sizing: CardSizing,
    ): String {
        if (!show) return ""
        val dataUri = product.id?.let { images[it] }
        return if (dataUri != null) {
            """<img src="$dataUri" class="card-img" alt="${escape(product.name)}"/>"""
        } else {
            """<table style="width:100%;"><tr><td class="card-no-img">Sem imagem</td></tr></table>"""
        }
    }

    private fun descriptionBlock(
        product: Product,
        show: Boolean,
    ): String {
        if (!show || product.description.isBlank()) return ""
        val text = product.description.take(200)
        val suffix = if (product.description.length > 200) "..." else ""
        return """<p class="desc-block">${escape(text)}$suffix</p>"""
    }

    private fun detailRow(
        label: String,
        value: String,
    ) = """<tr><td class="detail-lbl">$label</td><td class="detail-val">$value</td></tr>"""

    private fun typeBadgeClass(type: ProductType) =
        when (type) {
            ProductType.PHYSICAL -> "badge-type-phys"
            ProductType.DIGITAL -> "badge-type-dig"
            ProductType.SERVICE -> "badge-type-svc"
        }

    private fun typeLabel(type: ProductType) =
        when (type) {
            ProductType.PHYSICAL -> "Físico"
            ProductType.DIGITAL -> "Digital"
            ProductType.SERVICE -> "Serviço"
        }

    private fun statusBadge(status: ResourceStatus): Pair<String, String> =
        if (status == ResourceStatus.ACTIVE) "badge-active" to "Ativo" else "badge-inactive" to "Inativo"

    private fun marginPct(
        price: BigDecimal,
        cost: BigDecimal,
    ): String {
        val pct =
            (price - cost)
                .divide(price, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .setScale(1, RoundingMode.HALF_UP)
        return "${fmt(pct)}%"
    }

    private fun avgPrice(products: List<Product>): BigDecimal {
        if (products.isEmpty()) return BigDecimal.ZERO
        return products.map { it.price }
            .fold(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal(products.size), 2, RoundingMode.HALF_UP)
    }

    private fun escape(text: String) = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")

    private fun fmt(value: BigDecimal) = String.format("%.2f", value).replace(".", ",")
}