package esi.roadside.assistance.client.main.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
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
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.AuthActivity
import esi.roadside.assistance.client.core.presentation.components.IconDialog
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.MainNavigate
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.util.isPermissionGranted
import kotlinx.coroutines.launch

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
            val requestSheetState = rememberModalBottomSheetState(true)
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            var isGranted by remember { mutableStateOf<Boolean?>(null) }
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                isGranted = it
            }
            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                IconDialog(
                    visible = isGranted == false,
                    onDismissRequest = {
                        isGranted = null
                    },
                    title = stringResource(R.string.notification_permission_title),
                    text = stringResource(R.string.notification_permission_text),
                    icon = Icons.Default.NotificationsActive,
                    okListener = {
                        isGranted = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val permission = Manifest.permission.POST_NOTIFICATIONS
                            if (isPermissionGranted(permission)) {
                                NotificationManagerCompat.from(this).areNotificationsEnabled()
                            } else {
                                launcher.launch(permission)
                            }
                        } else {
                            NotificationManagerCompat.from(this).areNotificationsEnabled()
                        }
                    },
                    cancelListener = {
                        isGranted = null
                    },
                )
            }
        }
    }

}