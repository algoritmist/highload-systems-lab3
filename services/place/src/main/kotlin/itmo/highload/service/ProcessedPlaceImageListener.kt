package itmo.highload.service

import itmo.highload.api.dto.PlaceImage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ProcessedPlaceImageListener(
    private val placeService: PlaceService
) {
    @KafkaListener(topics = ["processed-place-images"], groupId = "file-processing", containerFactory = "placeImageKafkaListenerContainerFactory")
    fun listen(placeImage: PlaceImage){
        placeService.updatePlaceImage(placeImage)
    }
}