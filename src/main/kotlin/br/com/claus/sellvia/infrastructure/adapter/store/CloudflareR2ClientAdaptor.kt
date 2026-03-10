package br.com.claus.sellvia.infrastructure.adapter.store

import br.com.claus.sellvia.application.port.store.SystemStoragePort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Component
class CloudflareR2ClientAdaptor(
    private val s3Client: S3Client,
    @Value("\${cloudflare-r2.bucket-name}") private val BUCKET_NAME: String,
) : SystemStoragePort {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CloudflareR2ClientAdaptor::class.java)
    }

    override fun store(
        file: ByteArray,
        path: String,
        filename: String,
        contentType: String,
    ): String {
        val objectKey = "${path.trim('/')}/$filename"

        return executeS3Operation("UPLOAD", objectKey, BUCKET_NAME) {
            val putRequest =
                PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .contentType(contentType)
                    .contentLength(file.size.toLong())
                    .build()

            s3Client.putObject(putRequest, RequestBody.fromBytes(file))
            objectKey
        }
    }

    override fun delete(key: String) {
        executeS3Operation("DELETE", key, BUCKET_NAME) {
            val deleteRequest =
                DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build()

            s3Client.deleteObject(deleteRequest)
        }
    }

    private fun <T> executeS3Operation(
        action: String,
        key: String,
        bucket: String,
        operation: () -> T,
    ): T {
        LOGGER.info("Starting R2 operation: [{}] | Key: [{}] | Bucket: [{}]", action, key, bucket)

        return try {
            val result = operation()
            LOGGER.info("Successfully completed R2 operation: [{}] | Key: [{}]", action, key)
            result
        } catch (e: Exception) {
            LOGGER.error(
                "Failed R2 operation: [{}] | Key: [{}] | Bucket: [{}] | Error: {}",
                action,
                key,
                bucket,
                e.message,
                e
            )
            throw e
        }
    }
}