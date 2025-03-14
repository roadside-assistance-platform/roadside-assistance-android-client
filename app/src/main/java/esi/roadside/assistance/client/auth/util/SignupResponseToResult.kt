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

suspend inline fun <reified T> signUpResponseToResult(
    response: HttpResponse,
): Result<T, AuthError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Error(AuthError.SERIALIZATION_ERROR)
            }
        }
        400 -> Error(AuthError.UserAlreadyExists)
        500 -> Error(AuthError.SERVERERROR)
        else -> Error(AuthError.UNKNOWN)
    }
}

suspend inline fun <reified T> safeAuth(
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
    return signUpResponseToResult(response)
}