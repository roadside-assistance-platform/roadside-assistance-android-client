package esi.roadside.assistance.client.auth.util.account

import androidx.credentials.PasswordCredential

data class CredManagerResult(val credentials: PasswordCredential? = null, val error: CredManagerError? = null)

data class CredManagerError(val errorMessage: String)