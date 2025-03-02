package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.core.data.dto.Comment
import java.time.ZonedDateTime
import java.util.Date

data class CommentModel(
    val id: String,
    val clientId: String,
    val providerId: String,
    val text: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
) {
    fun toComment() = Comment(
        id = id,
        clientId = clientId,
        providerId = providerId,
        text = text,
        createdAt = createdAt.toInstant().toEpochMilli(),
        updatedAt = updatedAt.toInstant().toEpochMilli()
    )
}
