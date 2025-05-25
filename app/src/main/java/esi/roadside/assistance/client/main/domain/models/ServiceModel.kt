package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.core.data.dto.Service
import esi.roadside.assistance.client.main.data.dto.mapbox.JsonDirectionsResponse
import esi.roadside.assistance.client.main.domain.Categories
import java.time.ZonedDateTime

data class ServiceModel(
    val id: String = "",
    val clientId: String = "",
    val providerId: String? = null,
    val price: Int = 0,
    val serviceRating: Float = 0f,
    val description: String? = null,
    val serviceLocation: LocationModel = LocationModel(0.0, 0.0),
    val serviceLocationString: String = "",
    val done: Boolean = false,
    val category: Categories = Categories.OTHER,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val updatedAt: ZonedDateTime = ZonedDateTime.now(),
    val comments: List<CommentModel> = emptyList()
) {
    fun toService() = Service(
        id = id,
        clientId = clientId,
        providerId = providerId,
        price = price,
        serviceRating = serviceRating,
        serviceLocation = serviceLocation.toString(),
        done = done,
        serviceCategory = category,
        createdAt = createdAt.toInstant().toString(),
        updatedAt = updatedAt.toInstant().toString(),
        comments = comments.map { it.toComment() }
    )
}

data class NotificationServiceModel(
    val id: String,
    val client: ClientInfo,
    val providerId: String?,
    val price: Int,
    val description: String,
    val serviceRating: Float,
    val serviceLocation: LocationModel,
    val serviceLocationString: String = "",
    val directions: JsonDirectionsResponse = JsonDirectionsResponse("", emptyList(), "", emptyList()),
    val done: Boolean,
    val category: Categories,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val comments: List<CommentModel>
)

