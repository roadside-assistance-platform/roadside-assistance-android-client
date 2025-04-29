package esi.roadside.assistance.client.core.data

import esi.roadside.assistance.client.BuildConfig

object Endpoints {
    const val LOGIN = "/client/login"
    const val SIGNUP = "/client/signup"
    const val UPDATE_PROFILE = "/client/update/"
    const val HOME = "/home"
    const val SEND_EMAIL = "/email/send-code"
    const val VERIFY_EMAIL = "/email/verify-code"
    const val SERVICE_CREATE = "/service/create"
    const val SERVICE_UPDATE = "/service/update/"
    const val MAPBOX_GEOCODING = "forward"
}


object BaseUrls {
    const val API = BuildConfig.BASE_URL
    const val MAPBOX_GEOCODING = BuildConfig.MAPBOX_GEOCODING
}