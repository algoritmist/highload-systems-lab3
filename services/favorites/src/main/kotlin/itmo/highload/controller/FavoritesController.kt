package itmo.highload.controller

import io.swagger.v3.oas.annotations.tags.Tag
import itmo.highload.api.dto.CreateFavoritesRequest
import itmo.highload.api.dto.response.FavoritesResponse
import itmo.highload.model.FavoritesMapper
import itmo.highload.service.FavoritesService
import jakarta.validation.Valid
import itmo.highload.security.jwt.JwtUtils
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/favorites")
@Tag(name = "favorites_controller", description = "Rest API for favorites service")
class FavoritesController(
    private val favoritesService: FavoritesService, private val jwtUtils: JwtUtils
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun addToFavorites(
        @Valid @RequestBody favoriteEntity: CreateFavoritesRequest,
        @RequestHeader("Authorization") token: String
    ): Mono<FavoritesResponse> {
        val userId = jwtUtils.extractUserId(token)
        return favoritesService.addToFavorites(token, favoriteEntity, userId).map { FavoritesMapper.toResponse(it) }

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getFavorite(@PathVariable id: String): Mono<FavoritesResponse> {
        return favoritesService.getFavorite(id).map { FavoritesMapper.toResponse(it) }

    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun getFavoritesByUser(
        @RequestHeader("Authorization") token: String
        ): Flux<FavoritesResponse> {
        val userId = jwtUtils.extractUserId(token)
        return favoritesService.getFavoritesByUser(userId).map { FavoritesMapper.toResponse(it) }

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun deleteFavorite(@PathVariable id: String,  @RequestHeader("Authorization") token: String):Mono<Void> {
        val userId = jwtUtils.extractUserId(token)
        return favoritesService.deleteFavorite(id, userId)
    }

    @DeleteMapping("/place/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('OWNER', 'USER')")
    fun deleteFavoritesForPlace(
        @PathVariable("id") id: String): Mono<Void> {
        return favoritesService.deleteFavoritesForPlace(id)
    }
}
