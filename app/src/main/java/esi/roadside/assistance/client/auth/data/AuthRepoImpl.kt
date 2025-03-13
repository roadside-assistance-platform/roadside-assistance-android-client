package esi.roadside.assistance.client.auth.data

import esi.roadside.assistance.client.auth.data.dto.LoginResponse
import esi.roadside.assistance.client.auth.data.dto.SignupRequest
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.auth.util.safeAuth
import esi.roadside.assistance.client.core.data.networking.HttpClientFactory
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking

class AuthRepoImpl(
    private val client: HttpClient,
) : AuthRepo {
    override suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, AuthError> {
        val remote = request.toLoginRequest()
        return safeAuth<LoginResponse> {
            client.post(constructUrl("/client/login")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toLoginResponseModel()
        }
    }

    override suspend fun signup(request: SignupModel): Result<SignupModel, AuthError> {
        val remote = request.toSignupRequest()
        println(request.email)
        return safeAuth<SignupRequest> {
            client.post(constructUrl("/client/create")) {
                setBody(remote)
            }.body()
        }.map { response ->
            response.toSignupModel()
        }
    }

    override suspend fun resetPassword(email: String): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun update(request: UpdateModel): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun googleLogin(): Result<ClientModel, NetworkError> {
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
/*    val io = auth.signup(SignupModel( "test8@example.com", "password123","name1", "1600000000",
        "https://www.photo-paysage.com/albums/userpics/10001/thumb_Balade-chemin-hautes-pyrenees-16.JPG"))
    when (io) {
        is Result.Success -> {
            io.map {
                println(" name " + it.fullName)
                println("email: " + it.email)
            }
        }

        is Result.Error -> println(
            "Error: ${io.error.name}"
        )
    }*/
    //test login
    val iow = auth.login(LoginRequestModel("test8@example.com", "password123"))
    when (iow) {
        is Result.Success -> {
            iow.map {
                println("Full name: " + it.client.fullName)
                println("email: " + it.client.email)
            }
        }

        is Result.Error -> {
            println("Error: ${iow.error.name}")
        }
    }
   // auth.authenticate()
}