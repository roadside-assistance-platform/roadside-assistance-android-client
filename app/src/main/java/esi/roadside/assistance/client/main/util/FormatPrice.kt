package esi.roadside.assistance.client.main.util

import java.util.Locale

fun Int.formatPrice() = String.format(Locale.getDefault(), "%d.00", this) + " DZD"
