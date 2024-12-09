package itmo.highload.config

import org.springframework.kafka.support.serializer.JsonSerializer
import itmo.highload.api.dto.PlaceImage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@EnableKafka
@Configuration
class KafkaFileProducerConfig {
    @Bean
    fun fileProducersConfig(): Map<String, Any>{
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9091,kafka2:9092,kafka3:9093",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
    }

    @Bean
    fun fileProducerFactory() : DefaultKafkaProducerFactory<String, PlaceImage>{
        return DefaultKafkaProducerFactory(fileProducersConfig())
    }

    @Bean
    fun fileProducerKafkaTemplate(): KafkaTemplate<String, PlaceImage> {
        return KafkaTemplate(fileProducerFactory())
    }

}