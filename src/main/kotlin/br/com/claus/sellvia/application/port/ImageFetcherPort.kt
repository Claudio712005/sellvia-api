package br.com.claus.sellvia.application.port

interface ImageFetcherPort {
    /**
     * Busca a imagem na URL informada e retorna uma data URI base64
     * pronta para uso em atributos `src` de `<img>`.
     *
     * Retorna `null` se a URL for vazia, inválida ou o recurso
     * estiver indisponível, sem lançar exceção.
     */
    fun fetchAsBase64DataUri(url: String): String?
}