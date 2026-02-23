package br.com.claus.sellvia.application.port.store

import br.com.claus.sellvia.domain.enums.FolderDestination

interface SystemStoragePort {

    fun store(
        file: ByteArray,
        path: String,
        filename: String,
        contentType: String,
    ): String

    fun delete(key: String)

}