package esi.roadside.assistance.client.auth.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.core.content.edit

class PersistentCookieStorage(context: Context) : CookiesStorage {
    private val mutex = Mutex()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE)
    private var cookie: Cookie? = null

    init {
        // Load cookies from SharedPreferences
        sharedPreferences.getString("cookie", "")?.takeIf { it.isNotBlank() }?.let { cookiesJson ->
            cookie = Json.decodeFromString<SerializableCookie>(cookiesJson).toCookie()
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        return cookie?.let { listOf(it) } ?: emptyList<Cookie>()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) = mutex.withLock {
        val urlString = requestUrl.encodedPath
        val fixedCookie = cookie.copy(
            domain = cookie.domain ?: requestUrl.host,
            path = cookie.path ?: urlString
        )
        this.cookie = fixedCookie
        val serializableCookies = SerializableCookie.fromCookie(fixedCookie)
        sharedPreferences.edit {
            putString("cookie", Json.encodeToString(serializableCookies))
        }
        logAllCookies()
    }

    suspend fun deleteCookie() = mutex.withLock {
        this.cookie = null
        sharedPreferences.edit {
            remove("cookie")
        }
        logAllCookies()
    }

    fun logAllCookies() {
        Log.d("CookieStorage", "--- All Stored Cookies ---")
        cookie?.let {
            Log.d("CookieStorage", "URL: ${it.path}")
            Log.d("CookieStorage", "  Cookie: ${it.name}=${it.value}")
            Log.d("CookieStorage", "    Domain: ${it.domain}, Path: ${it.path}")
            Log.d("CookieStorage", "    Expires: ${it.expires}, MaxAge: ${it.maxAge}")
            Log.d("CookieStorage", "    Secure: ${it.secure}, HttpOnly: ${it.httpOnly}")
        }
    }

    override fun close() {
        // No resources to release
    }

    @Serializable
    private data class SerializableCookie(
        val name: String,
        val value: String,
        val encoding: String,
        val maxAge: Int = 0,
        val expires: Long? = null,
        val domain: String? = null,
        val path: String? = null,
        val secure: Boolean = false,
        val httpOnly: Boolean = false,
        val extensions: Map<String, String?> = emptyMap()
    ) {
        companion object {
            fun fromCookie(cookie: Cookie): SerializableCookie {
                return SerializableCookie(
                    cookie.name,
                    cookie.value,
                    cookie.encoding.name,
                    cookie.maxAge ?: 0,
                    cookie.expires?.timestamp,
                    cookie.domain,
                    cookie.path,
                    cookie.secure,
                    cookie.httpOnly,
                    cookie.extensions
                )
            }
        }

        fun toCookie(): Cookie {
            return Cookie(
                name,
                value,
                CookieEncoding.valueOf(encoding),
                maxAge,
                expires?.let { GMTDate(it) },
                domain,
                path,
                secure,
                httpOnly,
                extensions
            )
        }
    }
}