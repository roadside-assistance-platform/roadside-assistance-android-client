package esi.roadside.assistance.client.auth.util


import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.Result.Error
import esi.roadside.assistance.client.core.domain.util.Result.Success
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

enum class AuthType {
    LOGIN,
    SIGNUP,
    RESET_PASSWORD,
    UPDATE,
}

suspend inline fun <reified T> responseToResult(
    authType: AuthType,
    response: HttpResponse,
): Result<T, AuthError> {
    val error400 = when (authType) {
        AuthType.SIGNUP -> AuthError.USER_ALREADY_EXISTS
        AuthType.LOGIN -> AuthError.USER_NOT_FOUND
        AuthType.RESET_PASSWORD -> AuthError.USER_NOT_FOUND
        AuthType.UPDATE -> AuthError.USER_NOT_FOUND
    }
    val error401 = when (authType) {
        AuthType.SIGNUP -> AuthError.USER_ALREADY_EXISTS
        AuthType.LOGIN -> AuthError.INTERNAL_ERROR
        AuthType.RESET_PASSWORD -> AuthError.INTERNAL_ERROR
        AuthType.UPDATE -> AuthError.INTERNAL_ERROR
    }
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Error(AuthError.SERIALIZATION_ERROR)
            }
        }
        400 -> Error(error400)
        401 -> Error(error401)
        500 -> Error(AuthError.SERVER_ERROR)
        else -> Error(AuthError.UNKNOWN)
    }
}

suspend inline fun <reified T> safeAuth(
    authType: AuthType,
    call: () -> HttpResponse,
): Result<T, AuthError> {
    val response = try {
        call()
    } catch (_: UnresolvedAddressException) {
        return Error(AuthError.NO_INTERNET)
    } catch (_: SerializationException) {
        return Error(AuthError.SERIALIZATION_ERROR)
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        return Error(AuthError.UNKNOWN)
    }
    return responseToResult(authType, response)
}