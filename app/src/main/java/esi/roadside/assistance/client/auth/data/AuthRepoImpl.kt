package esi.roadside.assistance.client.auth.data

import esi.roadside.assistance.client.auth.data.dto.LoginResponse
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.auth.util.AuthType
import esi.roadside.assistance.client.auth.util.safeAuth
import esi.roadside.assistance.client.core.data.dto.Client
import esi.roadside.assistance.client.core.data.networking.HttpClientFactory
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
            client.put(constructUrl("/client/update")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toClientModel()
        }
    }

    override suspend fun googleLogin(): Result<ClientModel, AuthError> {
        TODO("Not yet implemented")
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
    val auth = AuthRepoImpl(HttpClientFactory.create(CIO.create()))
    // test sign up
    auth.signup(SignupModel( "test10@example.com", "password12354","name1", "1600000000",
        "https://www.photo-paysage.com/albums/userpics/10001/thumb_Balade-chemin-hautes-pyrenees-16.JPG"))
        .onSuccess {
            println("id: ${it.id}")
        }
    //test login
    auth.login(LoginRequestModel("test8@example.com", "password123"))
        .onSuccess {
            println("Full name: " + it.client.fullName)
            println("email: " + it.client.email)
        }
        .onError {
            println("Error: ${it.name}")
        }
   // auth.authenticate()
}