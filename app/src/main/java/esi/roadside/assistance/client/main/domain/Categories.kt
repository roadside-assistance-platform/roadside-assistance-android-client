package esi.roadside.assistance.client.main.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import esi.roadside.assistance.client.R

enum class Categories(
    @StringRes
    val text: Int,
    @DrawableRes
    val icon: Int
) {
    TOWING(R.string.towing, R.drawable.auto_towing_24px),
    FLAT_TIRE(R.string.flat_tire, R.drawable.baseline_tire_repair_24),
    FUEL_DELIVERY(R.string.fuel_delivery, R.drawable.baseline_local_gas_station_24),
    LOCKOUT(R.string.lockout, R.drawable.baseline_lock_24),
    EMERGENCY(R.string.emergency, R.drawable.baseline_emergency_24),
    OTHER(R.string.other, R.drawable.baseline_question_mark_24)
}