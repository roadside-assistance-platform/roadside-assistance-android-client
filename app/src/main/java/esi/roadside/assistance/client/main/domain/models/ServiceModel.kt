package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.core.domain.Category
import esi.roadside.assistance.client.core.data.dto.Service
import esi.roadside.assistance.client.main.domain.Categories
import java.time.ZonedDateTime
import java.util.Date

data class ServiceModel(
    val id: String = "",
    val clientId: String = "",
    val providerId: String = "",
    val price: Int = 0,
    val serviceRating: Int = 0,
    val serviceLocation: LocationModel = LocationModel(0.0, 0.0),
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
        category = category,
        createdAt = createdAt.toInstant().toEpochMilli(),
        updatedAt = updatedAt.toInstant().toEpochMilli(),
        comments = comments.map { it.toComment() }
    )
}
