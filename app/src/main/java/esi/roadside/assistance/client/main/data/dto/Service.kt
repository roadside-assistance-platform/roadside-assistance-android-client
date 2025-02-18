package esi.roadside.assistance.client.main.data.dto

import esi.roadside.assistance.client.core.domain.Category
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@Serializable
data class Service(
    val id: String,
    val clientId: String,
    val providerId: String,
    val price: Int,
    val serviceRating: Int,
    val serviceLocation: String,
    val done: Boolean,
    val category: Category,
    val createdAt: Long,
    val updatedAt: Long,
    val comments: List<Comment>
) {
    fun toServiceModel() = ServiceModel(
        id = id,
        clientId = clientId,
        providerId = providerId,
        price = price,
        serviceRating = serviceRating,
        serviceLocation = LocationModel.fromString(serviceLocation),
        done = done,
        category = category,
        createdAt = Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()),
        updatedAt = Instant.ofEpochMilli(updatedAt).atZone(ZoneId.systemDefault()),
        comments = comments.map { it.toCommentModel() }
    )
}