package itmo.highload.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import itmo.highload.api.dto.ProcessPlaceImageRequest
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import itmo.highload.service.PlaceImageProducer
import itmo.highload.service.PlaceImageService
import java.util.*


@Controller
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "place_image_controller", description = "API for place image service")
class PlaceImageController(
    private val placeImageService: PlaceImageService,
    private val placeImageProducer: PlaceImageProducer
) {
    @MessageMapping("/app")
    fun processImage(request: ProcessPlaceImageRequest){
        val imageResource = placeImageService.processImage(
            request.placeId,
            Base64.getDecoder().decode(request.encodedImage)
        )
        placeImageProducer.send(imageResource)
    }
}
