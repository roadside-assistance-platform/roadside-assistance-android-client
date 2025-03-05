package esi.roadside.assistance.client.main.data.dto

import esi.roadside.assistance.client.main.domain.models.NotificationModel
import java.time.LocalDateTime

data class Notification(
    val id: String,
    val title: String,
    val text: String,
    val createdAt: LocalDateTime
) {
    fun toNotificationModel(): NotificationModel = NotificationModel(id, title, text, createdAt)
}
