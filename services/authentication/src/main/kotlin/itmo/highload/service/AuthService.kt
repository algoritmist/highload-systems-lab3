package itmo.highload.service

import itmo.highload.api.actions.AuthAction
import itmo.highload.api.actions.AuthActionType
import itmo.highload.api.events.AuthEvent
import itmo.highload.api.events.AuthEventType
import itmo.highload.dto.RegisterDto
import itmo.highload.model.User
import itmo.highload.security.dto.JwtResponse
import itmo.highload.security.jwt.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class AuthService(
    private val jwtProvider: JwtUtils,
    private val userService: UserService,
    private val encoder: PasswordEncoder
) {
    private val bannedUsers = HashMap<String, String>()
    @Autowired
    private lateinit var authEventKafkaTemplate: KafkaTemplate<String, AuthEvent>

    @KafkaListener(topics = ["auth-action"], groupId = "security-events", containerFactory = "authActionKafkaListenerContainerFactory")
    fun `take security action for user`(message: AuthAction){
        if(message.type == AuthActionType.BAN){
            bannedUsers[message.login] = message.comment
        }
        else{
            bannedUsers.remove(message.login)
        }
    }

    fun login(login: String, password: String): Mono<JwtResponse> = userService.getByLogin(login).flatMap { user ->
        if(bannedUsers.contains(login)){
            try {
                authEventKafkaTemplate.send(
                    "auth-event",
                    AuthEvent(AuthEventType.LOGIN_REATTEMPT, login, "Login reattempt", Instant.now())
                )
            }
            catch(e: Exception){
                e.printStackTrace()
            }
            Mono.error(BadCredentialsException(bannedUsers[login]))
        }
        else {
            if (!encoder.matches(password, user.password)) {
                try {
                    authEventKafkaTemplate.send(
                        "auth-event",
                        AuthEvent(AuthEventType.LOGIN_FAIL, login, "Wrong login or password", Instant.now())
                    )
                }
                catch(e: Exception){
                    e.printStackTrace()
                }
                Mono.error(BadCredentialsException("Wrong login or password"))
            } else {
                try{
                    authEventKafkaTemplate.send("auth-event",
                        AuthEvent(AuthEventType.LOGIN_SUCCESS, login, "Successful login", Instant.now()))
                }
                catch(e: Exception){
                    e.printStackTrace()
                }
                val accessToken: String = jwtProvider.generateAccessToken(user.login, user.role, user.id!!)
                Mono.just(JwtResponse(accessToken, user.role))
            }
        }
    }

    fun register(request: RegisterDto): Mono<User> = userService.addUser(request)

    fun checkIfUserExists(login: String): Mono<Boolean> = userService.checkIfExists(login)
}
