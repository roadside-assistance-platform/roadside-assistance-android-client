package esi.roadside.assistance.client.main.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.toStringDate(): String {
    return this.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}