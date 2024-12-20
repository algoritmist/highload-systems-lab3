package itmo.highload.config

import org.springframework.kafka.support.serializer.JsonSerializer
import itmo.highload.api.actions.AuthAction
import itmo.highload.api.events.AuthEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@EnableKafka
@Configuration
class KafkaProducerConfig {
    @Bean
    fun securityProducersConfig(): Map<String, Any>{
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9091,kafka2:9092,kafka3:9093",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
    }

    @Bean
    fun authEventProducerFactory() : DefaultKafkaProducerFactory<String, AuthEvent>{
        return DefaultKafkaProducerFactory(securityProducersConfig())
    }

    @Bean
    fun authActionProducerFactory() : DefaultKafkaProducerFactory<String, AuthAction>{
        return DefaultKafkaProducerFactory(securityProducersConfig())
    }

    @Bean
    fun authEventKafkaTemplate(): KafkaTemplate<String, AuthEvent> {
        return KafkaTemplate(authEventProducerFactory())
    }
    @Bean
    fun authActionKafkaTemplate(): KafkaTemplate<String, AuthAction>{
        return KafkaTemplate(authActionProducerFactory())
    }

}