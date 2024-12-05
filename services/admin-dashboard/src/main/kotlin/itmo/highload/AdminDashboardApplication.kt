package itmo.highload

import itmo.highload.config.KafkaConsumerConfig
import itmo.highload.config.KafkaProducerConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.config.EnableWebFlux
import reactivefeign.spring.config.EnableReactiveFeignClients

@EnableWebFlux
@EnableFeignClients
@EnableReactiveFeignClients
@EnableHystrix
@SpringBootApplication(
    exclude = [
        SecurityAutoConfiguration::class,
        UserDetailsServiceAutoConfiguration::class
    ]
)
class AdminDashboardApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AdminDashboardApplication>(*args)
}