package itmo.highload.config

import itmo.highload.api.dto.PlaceImage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaFileConsumerConfig {
    @Bean
    fun placeFileConsumerConfig(): Map<String, Any>{
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9091,kafka2:9092,kafka3:9093",
            ConsumerConfig.GROUP_ID_CONFIG to "processed-images",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class
        )
    }

    @Bean
    fun placeImageKafkaConsumerFactory(): ConsumerFactory<String, PlaceImage>{
        val serializer = JsonDeserializer<PlaceImage>()
        serializer.addTrustedPackages("*")
        return DefaultKafkaConsumerFactory(
            placeFileConsumerConfig(),
            StringDeserializer(),
            serializer
        )
    }

    @Bean
    fun placeImageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PlaceImage>{
        val factory = ConcurrentKafkaListenerContainerFactory<String, PlaceImage>()
        factory.consumerFactory = placeImageKafkaConsumerFactory()
        return factory
    }
}