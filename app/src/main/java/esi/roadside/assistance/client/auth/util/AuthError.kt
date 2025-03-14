package esi.roadside.assistance.client.auth.util

import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.domain.util.Error

enum class AuthError(val text: Int): Error {
    SERVER_ERROR(R.string.server_error),
    INTERNAL_ERROR(R.string.internal_error),
    USER_ALREADY_EXISTS(R.string.user_exists_error),
    USER_NOT_FOUND(R.string.user_not_found_error),
    SERIALIZATION_ERROR(R.string.serialization_error),
    UNKNOWN(R.string.unknown_error),
    NO_INTERNET(R.string.no_internet_error),
}

