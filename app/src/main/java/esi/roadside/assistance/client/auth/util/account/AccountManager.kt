package esi.roadside.assistance.client.auth.util.account

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class AccountManager(private val context: Context, private val googleIdOption: GetGoogleIdOption) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun checkCredentials(username: String, password: String): Boolean {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = context,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )

            val credential = credentialResponse.credential as? PasswordCredential
                ?: return false

            credential.id == username && credential.password == password
        } catch (e: GetCredentialCancellationException) {
            e.printStackTrace()
            false
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            false
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun signUp(username: String, password: String): SignUpResult {
        return try {
            credentialManager.createCredential(
                context = context,
                request = CreatePasswordRequest(
                    id = username,
                    password = password
                )
            )
            SignUpResult.Success(username)
        } catch (e: CreateCredentialCancellationException) {
            e.printStackTrace()
            SignUpResult.Cancelled
        } catch (e: CreateCredentialException) {
            e.printStackTrace()
            SignUpResult.Failure
        }
    }

    suspend fun signIn(): SignInResult {
        return try {
            val credentialResponse = credentialManager.getCredential(
                context = context,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )
            val credential = credentialResponse.credential as? PasswordCredential
                ?: return SignInResult.Failure
            SignInResult.Success(credential.id, credential.password)
        } catch (e: GetCredentialCancellationException) {
            e.printStackTrace()
            SignInResult.Cancelled
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            SignInResult.NoCredentials
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            SignInResult.Failure
        }
    }

    suspend fun googleSignIn(): GetCredentialResponse {
        val credentialManager = CredentialManager.create(context)
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return credentialManager.getCredential(
            request = request,
            context = context,
        )
    }
}