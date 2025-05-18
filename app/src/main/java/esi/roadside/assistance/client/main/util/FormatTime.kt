package esi.roadside.assistance.client.main.util

import java.time.LocalDateTime
import java.util.Locale

fun Long.formatTime(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60)) % 60
    val hours = (this / (1000 * 60 * 60)) % 24
    val formattedTime = StringBuilder()
    if (hours > 0) {
        formattedTime.append(String.format(Locale.getDefault(), "%02d:", hours))
    }
    formattedTime.append(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds))
    return formattedTime.toString()
}

fun LocalDateTime.formatTime(): String =
    atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli().formatTime()