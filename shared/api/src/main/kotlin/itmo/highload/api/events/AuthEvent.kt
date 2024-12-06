package itmo.highload.api.events

import java.time.Instant

data class AuthEvent(
    val type: AuthEventType,
    val login: String,
    val message: String,
    val timestamp: Instant) {
}

enum class AuthEventType{
    LOGIN_FAIL,
    LOGIN_SUCCESS,
    LOGIN_REATTEMPT // Try to login while beeing banned
}