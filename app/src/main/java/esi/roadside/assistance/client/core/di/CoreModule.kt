package esi.roadside.assistance.client.core.di

import android.util.Log
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.auth.data.PersistentCookieStorage
import esi.roadside.assistance.client.auth.data.PersistentCookieStorage.SerializableCookie
import esi.roadside.assistance.client.auth.presentation.AuthViewModel
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.data.networking.constructUrl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

val coreModule = module {
    single {
        HttpClient().config {
            defaultRequest {
                header(HttpHeaders.Accept, ContentType.Application.Json)
                url(constructUrl(BuildConfig.BASE_URL))
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("HttpClient", message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpCookies) {
                storage = get<PersistentCookieStorage>()
            }
//            install(Auth) {
//                bearer {
//                    refreshTokens {
//                        val token = client.get {
//                            markAsRefreshTokenRequest()
//                            url("refreshToken")
//                            parameter("refreshToken", get<LocalService>().getRefreshToken())
//                        }.body<Token>()
//                        BearerTokens(
//                            accessToken = token.bearerToken,
//                            refreshToken = token.refreshToken
//                        )
//                    }
//                }
//            }
        }
    }
    single { SettingsDataStore(androidContext()) }
    // not here ,impl in authModule
    //viewModelOf(::AuthViewModel)
}