package esi.roadside.assistance.client.settings.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import org.koin.android.ext.android.inject

class SettingsActivity : ComponentActivity() {
    val settingsDataStore by inject<SettingsDataStore>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SetSystemBarColors(settingsDataStore)

        }
    }
}
