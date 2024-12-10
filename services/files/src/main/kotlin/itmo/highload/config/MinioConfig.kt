package itmo.highload.config

import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig{
    @Bean
    fun minioClient():MinioClient{
        return MinioClient.builder()
            .endpoint("http://minio:9000")
            .credentials("minio", "minio")
            .build()
    }
}