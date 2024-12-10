package itmo.highload.controller

import itmo.highload.api.dto.ProcessPlaceImageRequest
import itmo.highload.service.WebSocketPlaceImageClient
import itmo.highload.exceptions.IllegalFileSizeException
import itmo.highload.exceptions.InvalidFileTypeException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class PlaceImageHandler(
    private val client: WebSocketPlaceImageClient
) {
    private val maxSize = 4 * 1024 * 1024
    private val expectedType = "image/"

    fun handleFile(placeId: String, file: MultipartFile){
        /*if(file.contentType?.startsWith(expectedType) != true){
            throw InvalidFileTypeException("Invalid file type")
        }
        if(file.size > maxSize){
            throw IllegalFileSizeException("File too big")
        }*/
        client.sendProcessPlaceImageRequest(
            ProcessPlaceImageRequest(
                placeId,
                Base64.getEncoder().encodeToString(file.bytes)
            ))
    }
}