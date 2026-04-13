package br.com.claus.sellvia.domain.enums

enum class CardStyle {
    /**
     * Estilo voltado para o cliente final.
     * Foco na imagem e no preço, layout limpo e atrativo.
     */
    CUSTOMER,

    /**
     * Estilo voltado para funcionários da empresa.
     * Layout denso com custo, margem de lucro calculada,
     * estoque e demais dados operacionais em evidência.
     */
    EMPLOYEE,
}