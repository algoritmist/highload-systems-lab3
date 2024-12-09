package itmo.highload.service

import com.netflix.discovery.EurekaClient
import itmo.highload.api.dto.ProcessPlaceImageRequest
import org.slf4j.LoggerFactory
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.net.URI
import java.util.concurrent.TimeUnit

@Component
class WebSocketPlaceImageClient(
    private val eurekaClient: EurekaClient
) {
    private val logger = LoggerFactory.getLogger(WebSocketPlaceImageClient::class.java)
    private var session: StompSession = makeNewSession()

    private fun makeNewSession(): StompSession {
        val instanceInfo = eurekaClient.getNextServerFromEureka("files-service", false)
        val client = StandardWebSocketClient()
        val stompClient = WebSocketStompClient(client)
        stompClient.messageConverter = MappingJackson2MessageConverter()
        val sessionHandler = PlaceImageServiceSessionHandler()
        val websocketHost = instanceInfo.homePageUrl.replace("http", "ws")
        val websocketEndpoint = "${websocketHost}app/files"
        val connectedSession = stompClient.connectAsync(
            URI.create(websocketEndpoint),
            null,
            null,
            sessionHandler
        )[5, TimeUnit.SECONDS]

        logger.info("Created new session")

        return connectedSession
    }

    private fun getSession(): StompSession {
        synchronized(session) {
            if (!session.isConnected) {
                session = makeNewSession()
            }
            return session
        }
    }

    fun sendProcessPlaceImageRequest(request: ProcessPlaceImageRequest) {
        val session = getSession()
        session.send("/app/process-image", request)
    }
}