package esi.roadside.assistance.client.auth.util

import esi.roadside.assistance.client.core.domain.util.Error
enum class AuthError: Error {
    Unauthorized,
    ServerError,
    Authentication_Failed,
    BadRequest_OR_UserAlreadyExists,


}

