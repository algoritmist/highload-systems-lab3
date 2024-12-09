package itmo.highload.service

import itmo.highload.api.dto.PlaceImage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class PlaceImageProducer(
    private val fileProducerKafkaTemplate: KafkaTemplate<String, PlaceImage>
) {
    fun send(image: PlaceImage){
        fileProducerKafkaTemplate.send("processed-place-images", image)
    }
}