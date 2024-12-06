package itmo.highload.config

import itmo.highload.api.actions.AuthAction
import itmo.highload.api.events.AuthEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

class KafkaConsumerConfig {
    @Bean
    fun securityConsumersConfig(): Map<String, Any>{
        return mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "kafka1:9091,kafka2:9092,kafka3:9093",
            ConsumerConfig.GROUP_ID_CONFIG to "security-events",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class
        )
    }

    @Bean
    fun authEventConsumerFactory(): ConsumerFactory<String, AuthEvent>{
        return DefaultKafkaConsumerFactory(
            securityConsumersConfig(),
            StringDeserializer(),
            JsonDeserializer<AuthEvent>()
        )
    }

    @Bean
    fun authActionConsumerFactory(): ConsumerFactory<String, AuthAction>{
        return DefaultKafkaConsumerFactory(
            securityConsumersConfig(),
            StringDeserializer(),
            JsonDeserializer<AuthAction>()
        )
    }

    @Bean
    fun authEventKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, AuthEvent>{
        val factory = ConcurrentKafkaListenerContainerFactory<String, AuthEvent>()
        factory.consumerFactory = authEventConsumerFactory()
        return factory
    }

    @Bean
    fun authActionKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, AuthAction>{
        val factory = ConcurrentKafkaListenerContainerFactory<String, AuthAction>()
        factory.consumerFactory = authActionConsumerFactory()
        return factory
    }
}