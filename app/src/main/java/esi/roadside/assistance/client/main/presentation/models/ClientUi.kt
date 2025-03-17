package esi.roadside.assistance.client.main.presentation.models

import android.net.Uri
import esi.roadside.assistance.client.auth.domain.models.UpdateModel

data class ClientUi(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val photo: Uri? = null
) {
    fun toUpdateModel() = UpdateModel(
        id = id,
        fullName = fullName,
        email = email,
        phone = phone
    )
}
