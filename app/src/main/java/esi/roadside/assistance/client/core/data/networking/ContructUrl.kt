package esi.roadside.assistance.client.core.data.networking

import esi.roadside.assistance.client.core.data.BaseUrls

fun constructUrl(
    url: String,
    baseUrl: String = BaseUrls.API,
): String {
    return when {
        url.contains(baseUrl) -> url
        url.startsWith("/") -> "${baseUrl}${url.drop(1)}"
        else -> "${baseUrl}$url"
    }
}