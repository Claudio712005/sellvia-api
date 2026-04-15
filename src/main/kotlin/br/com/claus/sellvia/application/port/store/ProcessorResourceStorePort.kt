package br.com.claus.sellvia.application.port.store

import br.com.claus.sellvia.domain.enums.FolderDestination

interface ProcessorResourceStorePort {
    fun saveOptimizedImage(
        imageBytes: ByteArray,
        companyId: Long,
        folderDestination: FolderDestination,
        resourceId: Long,
    ): String
}