package esi.roadside.assistance.client.core.data.networking

import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.Result.Error
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import kotlin.coroutines.coroutineContext


suspend inline fun <reified T> safeCall(
    callType: CallType? = null,
    call: () -> HttpResponse,
): Result<T, DomainError> {
    val response = try {
        call()
    } catch (_: UnresolvedAddressException) {
        return Error(DomainError.NO_INTERNET)
    } catch (_: SerializationException) {
        return Error(DomainError.SERIALIZATION_ERROR)
    } catch (_: ConnectException) {
        return Error(DomainError.NO_INTERNET)
    } catch (_: HttpRequestTimeoutException) {
        return Error(DomainError.TIMEOUT)
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        return Error(DomainError.UNKNOWN)
    }
    return responseToResult(callType, response)
}
