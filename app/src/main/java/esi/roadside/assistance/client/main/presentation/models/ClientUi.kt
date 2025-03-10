package esi.roadside.assistance.client.main.presentation.models

import android.net.Uri

data class ClientUi(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val picture: Uri? = null
)
