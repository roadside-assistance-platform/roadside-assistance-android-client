package esi.roadside.assistance.client.core.data

import esi.roadside.assistance.client.R
import android.content.Context
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.main.domain.models.LocationModel

object Endpoints {
    const val LOGIN = "/client/login"
    const val SIGNUP = "/client/signup"
    const val UPDATE_PROFILE = "/client/update/"
    const val HOME = "/home"
    const val SEND_EMAIL = "/email/send-code"
    const val SEND_FORGOT_EMAIL = "/email/forgot-code"
    const val VERIFY_EMAIL = "/email/verify-code"
    const val SERVICE_CREATE = "/service/create"
    const val SERVICE_UPDATE = "/service/update/"
    const val MAPBOX_GEOCODING = "forward"
    const val SERVICES = "/client/history/"
    const val COMPLETION_REQUEST = "/service/complete/"
    const val GET_LOCATION_STRING = "/reverse/geocoding?access_token={access_token}&longitude={longitude}&latitude={latitude}&types=address"
    const val GET_ROUTES = "/{profile}/{coordinates}?access_token={access_token}&geometries=geojson"

    fun getRoutesEndpoint(profile: String, coordinates: Pair<LocationModel, LocationModel>, context: Context) =
        GET_ROUTES.replace("{profile}", profile)
            .replace(
                "{coordinates}",
                "${coordinates.first};${coordinates.second}"
            )
            .replace(
                "{access_token}",
                context.getString(R.string.mapbox_access_token)
            )

    fun getLocationStringEndpoint(longitude: Double, latitude: Double, context: Context) =
        GET_LOCATION_STRING.replace("{access_token}", context.getString(R.string.mapbox_access_token))
            .replace("{longitude}", longitude.toString())
            .replace("{latitude}", latitude.toString())
}

object BaseUrls {
    const val API = BuildConfig.BASE_URL
    const val MAPBOX_GEOCODING = BuildConfig.MAPBOX_GEOCODING
    const val MAPBOX_DRIVING = BuildConfig.MAPBOX_DRIVING
}