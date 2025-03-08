package esi.roadside.assistance.client.main.util

import esi.roadside.assistance.client.main.domain.models.NotificationModel
import java.time.LocalDateTime

fun String.toNotificationModel(createdAt: LocalDateTime): NotificationModel {
    return NotificationModel("", "", "", false, "", createdAt)
}