package itmo.highload.controller

import itmo.highload.security.jwt.JwtUtils
import itmo.highload.service.PersonalAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${app.base-url}/place")
class PersonalAccountController(val placeService: PersonalAccountService) {
    @Autowired
    private lateinit var template : KafkaTemplate<String, String>

    @GetMapping("/sned")
    fun sendMessage(@RequestParam message: String) {
        template.send("example-topic", message)
    }
}