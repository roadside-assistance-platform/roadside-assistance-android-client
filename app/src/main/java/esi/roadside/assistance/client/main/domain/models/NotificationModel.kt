package esi.roadside.assistance.client.main.domain.models

import java.time.LocalDateTime

data class NotificationModel(
    val id: String,
    val title: String,
    val text: String,
    val createdAt: LocalDateTime
)
