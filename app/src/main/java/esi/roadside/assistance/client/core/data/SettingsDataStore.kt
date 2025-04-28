package esi.roadside.assistance.client.core.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import esi.roadside.assistance.client.core.util.dataFlow

class SettingsDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        val THEME_KEY = stringPreferencesKey("app_theme")
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
        val DYNAMIC_KEY = booleanPreferencesKey("dynamic_theme")
        val EXTRA_DARK_KEY = booleanPreferencesKey("extra_dark")
    }

    val theme = dataFlow(context.dataStore, THEME_KEY, "light")
    val dynamicColors = dataFlow(context.dataStore, DYNAMIC_KEY, false)
    val extraDark = dataFlow(context.dataStore, EXTRA_DARK_KEY, false)
    val language = dataFlow(context.dataStore, LANGUAGE_KEY, "system")

    suspend fun saveSettings(
        theme: String? = null,
        dynamic: Boolean? = null,
        extraDark: Boolean? = null,
        language: String? = null,
    ) {
        context.dataStore.edit { preferences ->
            theme?.let { preferences[THEME_KEY] = it }
            dynamic?.let { preferences[DYNAMIC_KEY] = it }
            extraDark?.let { preferences[EXTRA_DARK_KEY] = it }
            language?.let { preferences[LANGUAGE_KEY] = it }
        }
    }
}
