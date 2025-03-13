package esi.roadside.assistance.client.auth.util

import esi.roadside.assistance.client.core.domain.util.Error
enum class AuthError: Error {
    SERVERERROR,
    UserAlreadyExists,
    SERIALIZATION_ERROR,
    UNKNOWN,
    NO_INTERNET,

}

