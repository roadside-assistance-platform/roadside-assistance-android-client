package esi.roadside.assistance.client.auth.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Welcome : NavRoutes()

    @Serializable
    data object Login : NavRoutes()

    @Serializable
    data object Signup : NavRoutes()

    @Serializable
    data object VerifyEmail : NavRoutes()

    @Serializable
    data object ForgotPassword : NavRoutes()
}