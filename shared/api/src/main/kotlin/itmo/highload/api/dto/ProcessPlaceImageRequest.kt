package itmo.highload.api.dto

class ProcessPlaceImageRequest(
    val placeId: String,
    val token: String,
    val encodedImage: String
)