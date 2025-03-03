package esi.roadside.assistance.client.auth.domain.models

import android.net.Uri

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val phoneNumber: String,
    val image: Uri?
)
