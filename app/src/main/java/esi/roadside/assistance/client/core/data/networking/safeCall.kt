package esi.roadside.assistance.client.core.data.networking

import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    call: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        call()
    } catch (_: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (_: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }
    return responseToResult(response)
}