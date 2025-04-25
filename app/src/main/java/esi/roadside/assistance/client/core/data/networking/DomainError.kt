package esi.roadside.assistance.client.core.data.networking

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.ui.graphics.vector.ImageVector
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.domain.util.Error

enum class DomainError(val text: Int, val description: Int? = null, val icon: ImageVector? = null):
    Error {
    SERVER_ERROR(R.string.server_error),
    INTERNAL_ERROR(R.string.internal_error),
    USER_ALREADY_EXISTS(R.string.user_exists_error),
    INCORRECT_CREDENTIALS(R.string.incorrect_credentials_error),
    USER_NOT_FOUND(R.string.user_not_found_error),
    SERIALIZATION_ERROR(R.string.serialization_error),
    UNKNOWN(R.string.unknown_error),
    NO_INTERNET(R.string.no_internet_error, R.string.no_internet_error_descr, Icons.Default.WifiOff),
    EMAIL_ERROR(R.string.email_not_verified_error, icon = Icons.Default.Email),
    INCORRECT_VERIFICATION_CODE(R.string.incorrect_code, icon = Icons.Default.Email),
    TIMEOUT(R.string.timeout, icon = Icons.Default.Timer),
    GOOGLE_UNEXPECTED_ERROR(R.string.google_unexpected_error),
}