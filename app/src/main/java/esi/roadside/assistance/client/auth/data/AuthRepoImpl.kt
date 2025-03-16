package esi.roadside.assistance.client.auth.data

import android.util.Log
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import esi.roadside.assistance.client.auth.data.dto.LoginRequest
import esi.roadside.assistance.client.auth.data.dto.LoginResponse
import esi.roadside.assistance.client.auth.domain.models.GoogleLoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.HomeRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.auth.util.AuthType
import esi.roadside.assistance.client.auth.util.safeAuth
import esi.roadside.assistance.client.core.data.dto.Client
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking

class AuthRepoImpl(
    private val client: HttpClient,
) : AuthRepo {
    override suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, AuthError> {
        val remote = request.toLoginRequest()
        return safeAuth<LoginResponse>(AuthType.LOGIN) {
            client.post(constructUrl("/client/login")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toLoginResponseModel()
        }
    }

    override suspend fun signup(request: SignupModel): Result<ClientModel, AuthError> {
        val remote = request.toSignupRequest()
        return safeAuth<Client>(AuthType.SIGNUP) {
            client.post(constructUrl("/client/signup")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toClientModel()
        }
    }

    override suspend fun resetPassword(email: String): Result<ClientModel, AuthError> {
        TODO("Not yet implemented")
    }

    override suspend fun update(request: UpdateModel): Result<ClientModel, AuthError> {
        val remote = request.toUpdateRequest()
        return safeAuth<Client>(AuthType.UPDATE) {
            client.put(constructUrl("/client/update/${request.id}")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toClientModel()
        }
    }

    override suspend fun home(): Result<Boolean, AuthError> {
        return safeAuth<String>(AuthType.HOME) {
            client.get(constructUrl("/")).body()
        }.map { response ->
            response == "Success! You are in home."
        }
    }

    override suspend fun googleLogin(result: GetCredentialResponse): Result<ClientModel, AuthError> {
        val credential = result.credential
        return when (credential) {
            is PublicKeyCredential -> {
                safeAuth<ClientModel>(AuthType.GOOGLE) {
                    client.post(constructUrl("/google/verify")) {
                        setBody(credential.authenticationResponseJson)
                    }.body()
                }
            }
            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                Log.d("AuthRepoImpl", "Username: $username, Password: $password")
                safeAuth<ClientModel>(AuthType.GOOGLE) {
                    client.post(constructUrl("/client/login")) {
                        setBody(LoginRequest(username, password))
                    }.body()
                }
            }
            else -> {
                Result.Error(AuthError.GOOGLE_UNEXPECTED_ERROR)
            }
        }
    }

    override suspend fun googleOldLogin(idToken: String): Result<LoginResponseModel, AuthError> {
        val remote = GoogleLoginRequestModel(idToken)
        return safeAuth<LoginResponse>(AuthType.GOOGLE) {
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