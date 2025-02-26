package esi.roadside.assistance.client.core.data.networking

import esi.roadside.assistance.client.BuildConfig

fun constructUrl(
    url: String
): String {
    return when {
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> "${BuildConfig.BASE_URL}${url.drop(1)}"
        else -> "${BuildConfig.BASE_URL}$url"
    }
}