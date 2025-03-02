package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.core.domain.Category
import esi.roadside.assistance.client.core.data.dto.Service
import java.time.ZonedDateTime
import java.util.Date

data class ServiceModel(
    val id: String,
    val clientId: String,
    val providerId: String,
    val price: Int,
    val serviceRating: Int,
    val serviceLocation: LocationModel,
    val done: Boolean,
    val category: Category,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val comments: List<CommentModel>
) {
    fun toService() = Service(
        id = id,
        clientId = clientId,
        providerId = providerId,
        price = price,
        serviceRating = serviceRating,
        serviceLocation = serviceLocation.toString(),
        done = done,
        category = category,
        createdAt = createdAt.toInstant().toEpochMilli(),
        updatedAt = updatedAt.toInstant().toEpochMilli(),
        comments = comments.map { it.toComment() }
    )
}
