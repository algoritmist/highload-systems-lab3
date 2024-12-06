package itmo.highload.config

import itmo.highload.api.actions.AuthAction
import itmo.highload.api.actions.AuthActionType
import itmo.highload.api.events.AuthEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@EnableKafka
@Configuration
class KafkaProducerConfig {
    @Bean
    fun securityProducersConfig(): Map<String, Any>{
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9091,kafka2:9092,kafka3:9093",
            ConsumerConfig.GROUP_ID_CONFIG to "security-events",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG  to StringDeserializer::class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
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