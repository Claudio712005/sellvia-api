package br.com.claus.sellvia.infrastructure.adapter.store

import br.com.claus.sellvia.application.port.store.ImageProcessorPort
import br.com.claus.sellvia.application.port.store.ProcessorResourceStorePort
import br.com.claus.sellvia.application.port.store.SystemStoragePort
import br.com.claus.sellvia.domain.enums.FolderDestination
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProcessorResourceStoreAdapter(
    val optimizer: ImageProcessorPort,
    val sysStore: SystemStoragePort,
) : ProcessorResourceStorePort {
    override fun saveOptimizedImage(
        imageBytes: ByteArray,
        companyId: Long,
        folderDestination: FolderDestination,
        resourceId: Long,
    ): String {
        val optimizedImage = optimizer.optimize(imageBytes)

        val path = folderDestination.buildPath(companyId, resourceId)

        return sysStore.store(
            file = optimizedImage.bytes,
            path = path,
            filename = "${UUID.randomUUID()}.webp",
            contentType = "image/webp",
        )
    }
}