package esi.roadside.assistance.client.main.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.auth.presentation.AuthActivity
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.MainNavigate
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.util.isPermissionGranted
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val permissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
    else
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetSystemBarColors()
            val navController = rememberNavController()
            val requestSheetState = rememberModalBottomSheetState(true)
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            var isGranted by remember { mutableStateOf<Map<String, Boolean?>>(
                mapOf()
            ) }
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { granted ->
                isGranted = granted
                if (granted[Manifest.permission.POST_NOTIFICATIONS] == true) {
                    NotificationManagerCompat.from(this).areNotificationsEnabled()
                }
            }
            LaunchedEffect(Unit) {
                isGranted = permissions.associateWith {
                    isPermissionGranted(it).takeIf { it == true }
                }
            }
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
                    requestSheetState = requestSheetState,
                )
                PermissionsDialog(
                    isGranted = isGranted,
                    refresh = {
                        isGranted = permissions.associateWith {
                            isPermissionGranted(it)
                        }
                    }
                ) {
                    val denied = permissions.filter { isGranted[it] == false }
                    if (denied.isNotEmpty()) {
                        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = android.net.Uri.fromParts("package", packageName, null)
                        }
                        startActivity(intent)
                    } else {
                        launcher.launch(permissions)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra("from_notification")) {
            Log.d("MainActivity", "from_notification")
        }
    }
}

