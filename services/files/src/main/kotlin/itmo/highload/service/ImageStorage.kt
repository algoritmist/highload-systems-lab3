package itmo.highload.service

import io.minio.MinioClient
import io.minio.PutObjectArgs
import itmo.highload.config.MinioConfig
import itmo.highload.exceptions.S3UploadException
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.net.URI

@Component
class ImageStorage(
    private val minioClient: MinioClient
) {
    private val bucketName = "places"

    fun storeFile(placeId: String, bytes: ByteArray): String {
        val stream = ByteArrayInputStream(bytes)
        val response = minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(placeId)
                .contentType("image/")
                .stream(stream, bytes.size.toLong(), -1)
                .build()
        )
        return "http://minio:9000/$bucketName/$placeId"
    }
}