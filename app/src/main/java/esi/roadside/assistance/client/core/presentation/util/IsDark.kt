package esi.roadside.assistance.client.core.presentation.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import esi.roadside.assistance.client.core.data.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@Composable
fun isDark(): Flow<Boolean> {
    val datastore = SettingsDataStore(LocalContext.current)
    val isSystemDark = isSystemInDarkTheme()
    return datastore.theme.map {
        when (it) {
            "light" -> false
            "dark" -> true
            else -> isSystemDark
        }
    }
}
