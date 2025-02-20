package esi.roadside.assistance.client.auth.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import org.koin.android.ext.android.inject
import kotlin.getValue

class AuthActivity : ComponentActivity() {
    val settingsDataStore by inject<SettingsDataStore>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetSystemBarColors(settingsDataStore)
            AppTheme {

            }
        }
    }
}