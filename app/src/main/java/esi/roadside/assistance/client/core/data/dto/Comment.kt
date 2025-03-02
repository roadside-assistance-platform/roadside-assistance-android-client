package esi.roadside.assistance.client.core.data.dto

import esi.roadside.assistance.client.main.domain.models.CommentModel
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class Comment(
    val id: String,
    val clientId: String,
    val providerId: String,
    val text: String,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toCommentModel() = CommentModel(
        id = id,
        clientId = clientId,
        providerId = providerId,
        text = text,
        createdAt = Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()),
        updatedAt = Instant.ofEpochMilli(updatedAt).atZone(ZoneId.systemDefault())
    )
}
