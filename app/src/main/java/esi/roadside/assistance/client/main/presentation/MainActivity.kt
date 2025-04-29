package esi.roadside.assistance.client.main.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import esi.roadside.assistance.client.auth.presentation.AuthActivity
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.MainNavigate
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
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
                    }

                    override fun onPermissionResult(granted: Boolean) {
                    }
                }
            )
            permissionsManager.requestLocationPermissions(this)
        }
        setContent {
            SetSystemBarColors()
            val navController = rememberNavController()
            val mainViewModel : MainViewModel = koinViewModel()
            val requestSheetState = rememberModalBottomSheetState(true)
            val serviceSheetState = rememberModalBottomSheetState(true)
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            CollectEvents {
                when(it) {
                    is MainNavigate -> navController.navigate(it.route)
                    is Event.DismissSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                    is Event.ShowMainActivityMessage ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message = getString(it.text))
                        }
                    is Event.ShowMainActivityActionSnackbar ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = getString(it.text),
                                    actionLabel = getString(it.actionText)
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> it.callback()
                                SnackbarResult.Dismissed -> Unit
                            }
                        }
                    Event.ShowRequestAssistance -> scope.launch {
                        requestSheetState.show()
                    }
                    Event.HideRequestAssistance -> scope.launch {
                        requestSheetState.hide()
                    }
                    Event.ExitToAuthActivity -> {
                        startActivity(Intent(this, AuthActivity::class.java))
                        finish()
                    }
                    else -> Unit
                }
            }
            AppTheme {
                AppScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    mainViewModel = mainViewModel,
                    requestSheetState = requestSheetState,
                    serviceSheetState = serviceSheetState
                )
            }
        }
    }

}