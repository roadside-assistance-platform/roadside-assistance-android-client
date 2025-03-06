package esi.roadside.assistance.client.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.util.CollectNotifications
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    val settingsDataStore by inject<SettingsDataStore>()
    lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = PermissionsManager(
                object : PermissionsListener {
                    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                        TODO("Not yet implemented")
                    }

                    override fun onPermissionResult(granted: Boolean) {
                        TODO("Not yet implemented")
                    }
                }
            )
            permissionsManager.requestLocationPermissions(this)
        }
        setContent {
            SetSystemBarColors(settingsDataStore)
            val navController = rememberNavController()
            val mainViewModel = get<MainViewModel>()
            CollectEvents {
                when(it) {
                    else -> Unit
                }
            }
            CollectNotifications {
                // TODO: Handle notification
            }
            AppTheme {
                NavigationScreen(navController, mainViewModel, onAction = mainViewModel::onAction)
            }
        }
    }

}