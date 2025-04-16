package esi.roadside.assistance.client.auth.data

import android.util.Log
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import esi.roadside.assistance.client.auth.data.dto.LoginRequest
import esi.roadside.assistance.client.auth.data.dto.LoginResponse
import esi.roadside.assistance.client.auth.domain.models.GoogleLoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.dto.Client
import esi.roadside.assistance.client.core.data.networking.CallType
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking

class AuthRepoImpl(
    private val persistentCookieStorage: PersistentCookieStorage,
    private val client: HttpClient,
) : AuthRepo {
    override suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, DomainError> {
        val remote = request.toLoginRequest()
        return safeCall<LoginResponse>(CallType.LOGIN) {
            client.post(constructUrl("/client/login")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toLoginResponseModel()
        }
    }

    override suspend fun signup(request: SignupModel): Result<LoginResponseModel, DomainError> {
        val remote = request.toSignupRequest()
        return safeCall<LoginResponse>(CallType.SIGNUP) {
            client.post(constructUrl("/client/signup")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toLoginResponseModel()
        }
    }

    override suspend fun resetPassword(email: String): Result<ClientModel, DomainError> {
        TODO("Not yet implemented")
    }

    override suspend fun update(request: UpdateModel): Result<ClientModel, DomainError> {
        persistentCookieStorage.logAllCookies()
        val remote = request.toUpdateRequest()
        return safeCall<Client>(CallType.UPDATE) {
            client.put(constructUrl("/client/update/${request.id}")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toClientModel()
        }
    }

    override suspend fun authHome(): Result<Boolean, DomainError> {
        return safeCall<String>(CallType.HOME) {
            client.get(constructUrl("/home"))
        }.map { response ->
            response == "Success! You are in home."
        }
    }

    override suspend fun googleLogin(result: GetCredentialResponse): Result<ClientModel, DomainError> {
        val credential = result.credential
        return when (credential) {
            is PublicKeyCredential -> {
                safeCall<ClientModel>(CallType.GOOGLE) {
                    client.post(constructUrl("/google/verify")) {
                        setBody(credential.authenticationResponseJson)
                    }.body()
                }
            }
            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                Log.d("AuthRepoImpl", "Username: $username, Password: $password")
                safeCall<ClientModel>(CallType.GOOGLE) {
                    client.post(constructUrl("/client/login")) {
                        setBody(LoginRequest(username, password))
                    }.body()
                }
            }
            else -> {
                Result.Error(DomainError.GOOGLE_UNEXPECTED_ERROR)
            }
        }
    }

    override suspend fun googleOldLogin(idToken: String): Result<LoginResponseModel, DomainError> {
        val remote = GoogleLoginRequestModel(idToken)
        return safeCall<LoginResponse>(CallType.GOOGLE) {
            client.post(constructUrl("/google/verify")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toLoginResponseModel()
        }
    }

    //home in routs
    override suspend fun authenticate() {

        try {
            val response: HttpResponse = client.get(constructUrl("/home"))

            when (response.status) {
                HttpStatusCode.OK -> {
                    println("Success! You are in here.")
                }

                HttpStatusCode.Found -> {
                    println("Redirecting to login page...")
                }

                else -> {
                    println("Unexpected response: ${response.status}")
                }
            }
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        } finally {
            client.close()
        }
    }
}


fun main(): Unit = runBlocking {
    // login
    //val context = Application().applicationContext
//    val auth = AuthRepoImpl(HttpClientFactory.create(CIO.create()))
//    // test sign up
//    auth.signup(SignupModel( "test10@example.com", "password12354","name1", "1600000000",
//        "https://www.photo-paysage.com/albums/userpics/10001/thumb_Balade-chemin-hautes-pyrenees-16.JPG"))
//        .onSuccess {
//            println("id: ${it.id}")
//        }
//    //test login
//    auth.login(LoginRequestModel("test8@example.com", "password123"))
//        .onSuccess {
//            println("Full name: " + it.client.fullName)
//            println("email: " + it.client.email)
//        }
//        .onError {
//            println("Error: ${it.name}")
//        }
   // auth.authenticate()
}