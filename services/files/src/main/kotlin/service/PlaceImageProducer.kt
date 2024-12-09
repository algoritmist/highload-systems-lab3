package service

import itmo.highload.api.dto.PlaceImage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class PlaceImageProducer(
    private val imageFileKafkaTemplate: KafkaTemplate<String, PlaceImage>
) {
    fun send(image: PlaceImage){
        imageFileKafkaTemplate.send("processed-place-images", image)
    }
}