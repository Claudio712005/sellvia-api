package br.com.claus.sellvia.application.port.store

interface SystemStoragePort {
    fun store(
        file: ByteArray,
        path: String,
        filename: String,
        contentType: String,
    ): String

    fun delete(key: String)

    fun buildFileUrl(key: String?): String
}