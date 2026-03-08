package br.com.claus.sellvia.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Configuration
class CloudflareR2Config {

    @Value("\${cloudflare-r2.account-id}")
    private lateinit var accountId: String

    @Value("\${cloudflare-r2.access-key}")
    private lateinit var accessKey: String

    @Value("\${cloudflare-r2.secret-key}")
    private lateinit var secretKey: String

    @Bean
    fun s3Client(): S3Client {
        val endpoint = "https://$accountId.r2.cloudflarestorage.com"

        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .region(Region.US_EAST_1)
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(false)
                    .chunkedEncodingEnabled(false)
                    .build()
            )
            .build()
    }
}