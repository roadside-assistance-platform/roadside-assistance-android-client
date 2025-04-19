package esi.roadside.assistance.client.auth.presentation.screens.signup

data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
)