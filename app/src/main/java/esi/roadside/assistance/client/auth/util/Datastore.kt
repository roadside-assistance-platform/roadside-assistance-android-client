package esi.roadside.assistance.client.auth.util

import android.content.Context
import androidx.datastore.dataStore
import esi.roadside.assistance.client.auth.UserPreferencesSerializer

val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)