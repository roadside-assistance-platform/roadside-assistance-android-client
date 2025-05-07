package esi.roadside.assistance.client.auth.domain.models

data class ResetPasswordModel(
    val email: String,
    val newPassword: String,
) {
    fun toDto() = esi.roadside.assistance.client.auth.data.dto.ResetPassword(
        email = email,
        newPassword = newPassword
    )
}
