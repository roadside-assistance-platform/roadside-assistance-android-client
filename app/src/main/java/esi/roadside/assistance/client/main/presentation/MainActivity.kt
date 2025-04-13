package esi.roadside.assistance.client.main.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.MainNavigate
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.util.CollectNotifications
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    lateinit var permissionsManager: PermissionsManager

    @OptIn(ExperimentalMaterial3Api::class)
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
            SetSystemBarColors()
            val navController = rememberNavController()
            val mainViewModel : MainViewModel = koinViewModel()
            val bottomSheetState = rememberModalBottomSheetState(true)
            val scope = rememberCoroutineScope()
            CollectEvents {
                when(it) {
                    is MainNavigate -> navController.navigate(it.route)
                    is Event.ShowMainActivityToast ->
                        Toast.makeText(this, getString(it.text), Toast.LENGTH_SHORT).show()
                    Event.ShowRequestAssistance -> scope.launch {
                        bottomSheetState.show()
                    }
                    Event.HideRequestAssistance -> scope.launch {
                        bottomSheetState.hide()
                    }
                    else -> Unit
                }
            }
            CollectNotifications {
                // Handle notification
            }
            AppTheme {
                NavigationScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    bottomSheetState = bottomSheetState,
                    onAction = mainViewModel::onAction
                )
            }
        }
    }

}