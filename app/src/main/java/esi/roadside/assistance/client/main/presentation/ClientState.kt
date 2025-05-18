package esi.roadside.assistance.client.main.presentation

import esi.roadside.assistance.client.R

enum class ClientState(val title: Int) {
    IDLE(R.string.idle),
    ASSISTANCE_REQUESTED(R.string.looking_for_providers),
    PROVIDER_IN_WAY(R.string.provider_in_way),
    ASSISTANCE_IN_PROGRESS(R.string.assistance_in_progress),
    ASSISTANCE_COMPLETED(R.string.assistance_completed),
    ASSISTANCE_FAILED(R.string.assistance_failed)
}