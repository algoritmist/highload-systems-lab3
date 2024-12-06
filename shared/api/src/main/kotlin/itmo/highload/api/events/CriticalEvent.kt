package itmo.highload.api.events

import java.time.Instant

class CriticalEvent(type: CriticalEventType, message: String, timestamp: Instant) {
}

enum class CriticalEventType{
    DATABASE_FAILED
}