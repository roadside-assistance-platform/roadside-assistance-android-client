package esi.roadside.assistance.client.core.data.dto

import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class Service(
    val id: String = "",
    val clientId: String = "",
    val providerId: String? = null,
    val price: Int = 0,
    val serviceRating: Float = 0f,
    val serviceLocation: String = "",
    val done: Boolean = false,
    val serviceCategory: Categories = Categories.OTHER,
    val createdAt: String = "",
    val updatedAt: String = "",
    val comments: List<Comment> = emptyList()
) {
    fun toServiceModel(locationString: String = "") = ServiceModel(
        id = id,
        clientId = clientId,
        providerId = providerId,
        price = price,
        serviceRating = serviceRating,
        serviceLocation = LocationModel.fromString(serviceLocation),
        serviceLocationString = locationString,
        done = done,
        category = serviceCategory,
        createdAt = Instant.parse(createdAt).atZone(ZoneId.systemDefault()),
        updatedAt = Instant.parse(updatedAt).atZone(ZoneId.systemDefault()),
        comments = comments.map { it.toCommentModel() }
    )
}