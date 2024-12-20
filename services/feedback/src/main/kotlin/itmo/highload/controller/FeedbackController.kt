package itmo.highload.controller

import io.swagger.v3.oas.annotations.tags.Tag
import itmo.highload.api.dto.CreatePlaceFeedbackRequest
import itmo.highload.api.dto.CreateRouteFeedbackRequest
import itmo.highload.api.dto.response.PlaceFeedbackResponse
import itmo.highload.api.dto.response.RouteFeedbackResponse
import itmo.highload.model.FeedbackMapper
import itmo.highload.security.jwt.JwtUtils
import itmo.highload.service.FeedbackService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/feedback")
@Tag(name = "feedback_controller", description = "Rest API for favorites service")
class FeedbackController(
    private val feedbackService: FeedbackService, private val jwtUtils: JwtUtils
) {

    @PostMapping("/route")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun createRouteFeedback(
        @Valid @RequestBody feedback: CreateRouteFeedbackRequest,
        @RequestHeader("Authorization") token: String
    ): Mono<RouteFeedbackResponse> {
        val userId = jwtUtils.extractUserId(token)
        return feedbackService.createRouteFeedback(feedback, token, userId).map { FeedbackMapper.toRouteResponse(it) }

    }

    @GetMapping("/route/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getRouteFeedbacks(
        @PathVariable id: String,
    ): Flux<RouteFeedbackResponse> {
        return feedbackService.getRouteFeedbacks(id).map { FeedbackMapper.toRouteResponse(it)}

    }

    @DeleteMapping("/route/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun deleteRouteFeedback(@PathVariable id: String): Mono<Void> {
        return feedbackService.deleteRouteFeedback(id)

    }

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun createPlaceFeedback(
        @Valid @RequestBody feedback: CreatePlaceFeedbackRequest,
        @RequestHeader("Authorization") token: String
    ): Mono<PlaceFeedbackResponse> {
        val userId = jwtUtils.extractUserId(token)
        return feedbackService.createPlaceFeedback(feedback, token, userId).map { FeedbackMapper.toPlaceResponse(it) }
    }

    @GetMapping("/place/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getPlaceFeedbacks(
        @PathVariable id: String,
    ): Flux<PlaceFeedbackResponse> {
        return feedbackService.getPlaceFeedbacks(id).map { FeedbackMapper.toPlaceResponse(it) }
    }

    @DeleteMapping("/place/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun deletePlaceFeedback(@PathVariable id: String): Mono<Void> {
        return feedbackService.deletePlaceFeedback(id)
    }

    @DeleteMapping("/place/batch/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER')")
    fun deleteFeedbacksForPlace(
        @PathVariable("id") id: String): Mono<Void> {
        return feedbackService.deleteFeedbacksForPlace(id)
    }

    @DeleteMapping("/route/batch/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER')")
    fun deleteFeedbacksForRoute(
        @PathVariable("id") id: String): Mono<Void> {
        return feedbackService.deleteFeedbacksForRoute(id)
    }
}
