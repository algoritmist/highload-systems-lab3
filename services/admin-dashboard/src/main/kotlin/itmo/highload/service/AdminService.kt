package itmo.highload.service

import itmo.highload.api.actions.AuthAction
import itmo.highload.api.actions.AuthActionType
import itmo.highload.api.events.AuthEvent
import itmo.highload.api.events.AuthEventType
import itmo.highload.api.events.CriticalEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class AdminService {
    private val suspiciousUsers = HashMap<String, Int>()
    private val bannedUsers = HashMap<String, Instant>()
    private val criticalEvents = HashMap<CriticalEvent, Int>()
    @Autowired
    private lateinit var authActionKafkaTemplate: KafkaTemplate<String, AuthAction>

    @KafkaListener(topics = ["auth-event"], groupId = "security-events")
    fun `ban suspicious users`(message: AuthEvent){
        val login = message.login
        if(message.type == AuthEventType.LOGIN_SUCCESS){
            if(bannedUsers.containsKey(login)){
                // TODO: report critical security issue!
            }
            suspiciousUsers.remove(login)
        }
        if(message.type == AuthEventType.LOGIN_FAIL){
            val count = (suspiciousUsers[login] ?: 0) + 1
            if(count >= 3){
                suspiciousUsers.remove(login)
                bannedUsers[login] = Instant.now()
                // ban user
                authActionKafkaTemplate.send("auth-action",
                    AuthAction(
                        login,
                        AuthActionType.BAN,
                        "You've been temporary banned for suspicious activity")
                )
            }
            else{
                suspiciousUsers[login] = count
            }
        }
        if(message.type == AuthEventType.LOGIN_REATTEMPT){
            if(!bannedUsers.containsKey(login)){
                // TODO: report bug and remove from suspicious users
            }
            else{
                if(Duration.between(bannedUsers[login]!!, Instant.now()).toMinutes() >= 15){
                    bannedUsers.remove(login)
                    authActionKafkaTemplate.send("auth-action",
                        AuthAction(
                            login,
                            AuthActionType.UNBAN,
                            "User unbanned"
                        )
                    )
                    // notify unban user
                }
            }
        }
    }
    @KafkaListener(topics = ["critical-event"], groupId = "security-events")
    fun `log critical events`(){

    }
}