package itmo.highload.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import itmo.highload.api.dto.PlaceDto
import itmo.highload.api.dto.UpdatePlaceDescriptionDto
import itmo.highload.api.dto.UpdatePlaceNameDto
import itmo.highload.api.dto.response.PlaceResponse
import itmo.highload.exceptions.EntityNotFoundException
import itmo.highload.model.Place
import itmo.highload.model.PlaceMapper
import itmo.highload.security.jwt.JwtUtils
import itmo.highload.service.PlaceImageServiceSessionHandler
import itmo.highload.service.PlaceService
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "place_controller", description = "Rest API for place service")
@RequestMapping("/place")
class PlaceController(
    val placeService: PlaceService,
    private val jwtUtils: JwtUtils,
    private val imageHandler: PlaceImageHandler
) {
    @GetMapping
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getAllPlaces(): Flux<PlaceResponse> = placeService.getAllPlaces().map { PlaceMapper.toPlaceResponse(it) }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getPlace(@PathVariable id: String, @RequestHeader("Authorization") token: String): Mono<PlaceResponse> {
        return placeService.getPlace(id).map { PlaceMapper.toPlaceResponse(it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('OWNER')")
    fun addPlace(
        @RequestBody @Valid request: PlaceDto,
        @RequestHeader("Authorization") token: String
    ): Mono<PlaceResponse> {
        val ownerId = jwtUtils.extractUserId(token)
        return placeService.addPlace(ownerId, request).map { PlaceMapper.toPlaceResponse(it) }
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("hasAuthority('OWNER')")
    fun updateName(
        @PathVariable id: String,
        @RequestBody @Valid request: UpdatePlaceNameDto,
        @RequestHeader("Authorization") token: String
    ): Mono<PlaceResponse> {
        val ownerId = jwtUtils.extractUserId(token)
        return placeService.updateName(ownerId, id, request.name).map { PlaceMapper.toPlaceResponse(it) }
    }

    @PatchMapping("/{id}/description")
    @PreAuthorize("hasAuthority('OWNER')")
    fun updateDescription(
        @PathVariable id: String,
        @RequestBody @Valid request: UpdatePlaceDescriptionDto,
        @RequestHeader("Authorization") token: String
    ): Mono<PlaceResponse> {
        val ownerId = jwtUtils.extractUserId(token)
        return placeService.updateDescription(ownerId, id, request.description).map { PlaceMapper.toPlaceResponse(it) }
    }

    @GetMapping("/near")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getPlacesNear(
        @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") latitude: Double,
        @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") longitude: Double,
        @RequestParam(required = false, defaultValue = "5.0") distanceKm: Double
    ): Flux<PlaceResponse> = placeService.getPlacesNear(latitude, longitude, distanceKm)
                                        .map { PlaceMapper.toPlaceResponse(it) }

    @GetMapping("/tag")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getPlacesNearByTag(
        @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") latitude: Double,
        @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") longitude: Double,
        @RequestParam(required = false, defaultValue = "5.0") distanceKm: Double,
        @RequestParam tag: String,
    ): Flux<PlaceResponse> = placeService.getPlacesNearByTag(latitude, longitude, distanceKm, tag)
                                        .map { PlaceMapper.toPlaceResponse(it) }

    @GetMapping("/name")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getPlacesNearByName(
        @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") latitude: Double,
        @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") longitude: Double,
        @RequestParam(required = false, defaultValue = "5.0") distanceKm: Double,
        @RequestParam name: String,
    ): Flux<PlaceResponse> = placeService.getPlacesNearByName(latitude, longitude, distanceKm, name)
                                        .map { PlaceMapper.toPlaceResponse(it) }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('OWNER')")
    fun deletePlace(
        @PathVariable id: String,
        @RequestHeader("Authorization") token: String
    ): Mono<Void> {
        val ownerId = jwtUtils.extractUserId(token)
        return placeService.deletePlace(id, ownerId, token)
    }

    @PostMapping("/upload-image/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('OWNER')")
    fun uploadPlaceImage(
        @PathVariable id: String,
        @RequestParam("file") file: MultipartFile,
        @RequestHeader("Authorization") token: String
    ) : Mono<String>{
        try {
            imageHandler.handleFile(id, file)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return Mono.just("Image processing, wait for operation to complete")
    }
}
