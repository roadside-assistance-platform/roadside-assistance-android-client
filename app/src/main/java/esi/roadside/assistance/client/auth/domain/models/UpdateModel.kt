package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.auth.data.dto.UpdateRequest

data class UpdateModel(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val photo: String?
) {
    fun toUpdateRequest() = UpdateRequest(
        fullName = fullName,
        phoneNumber = phoneNumber,
        photo = photo
    )
}
