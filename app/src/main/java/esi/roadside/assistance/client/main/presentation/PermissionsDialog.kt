package esi.roadside.assistance.client.main.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import esi.roadside.assistance.client.R

@Composable
fun PermissionsDialog(
    isGranted: Map<String, Boolean?>,
    refresh: () -> Unit,
    onRequestPermission: (Array<String>) -> Unit,
) {
    var visible by remember { mutableStateOf(true) }
    val permissionsStrings =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            mapOf(
                Manifest.permission.ACCESS_FINE_LOCATION to R.string.location_permission,
                Manifest.permission.POST_NOTIFICATIONS to R.string.notification_permission
            )
        else
            mapOf(
                Manifest.permission.ACCESS_FINE_LOCATION to R.string.location_permission,
            )
    val permissionsIcons =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            mapOf(
                Manifest.permission.ACCESS_FINE_LOCATION to Icons.Default.LocationOn,
                Manifest.permission.POST_NOTIFICATIONS to Icons.Default.Notifications
            )
        else
            mapOf(
                Manifest.permission.ACCESS_FINE_LOCATION to Icons.Default.LocationOn,
            )
    LaunchedEffect(isGranted) {
        if (isGranted.values.all { it == true } and isGranted.isNotEmpty()) {
            visible = false
        }
    }
    if (visible)
        Dialog(
            onDismissRequest = { visible = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 8.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Box(Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                        Image(
                            ImageVector.vectorResource(id = R.drawable.material_3_flower),
                            null,
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                        )
                        Icon(
                            Icons.Default.MiscellaneousServices,
                            null,
                            Modifier.fillMaxSize(.5f),
                            MaterialTheme.colorScheme.primary,
                        )
                    }
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            stringResource(R.string.permissions_required),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            stringResource(R.string.permissions_required_text),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        Modifier.padding(vertical = 8.dp)
                            .clip(MaterialTheme.shapes.large)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        isGranted.forEach { (permission, granted) ->
                            Row(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    permissionsIcons[permission]!!,
                                    null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.padding(start = 4.dp).size(24.dp)
                                )
                                Text(
                                    stringResource(permissionsStrings[permission]!!),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    {
                                        onRequestPermission(arrayOf(permission))
                                    },
                                    enabled = granted != true,
                                ) {
                                    Text(
                                        stringResource(
                                            if (granted == true) R.string.granted
                                            else if (granted == false) R.string.denied
                                            else R.string.request_permission
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(refresh) {
                            Text(stringResource(R.string.refresh))
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton({
                                visible = false
                            }) {
                                Text(stringResource(R.string.cancel))
                            }
                            Button({
                                onRequestPermission(isGranted.keys.toTypedArray())
                            }) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    }
                }
            }
        }
}

@Preview
@Composable
private fun DialogPreview() {
    PermissionsDialog(
        isGranted = mapOf(
            "ACCESS_FINE_LOCATION" to false,
            "ACCESS_COARSE_LOCATION" to false,
            "POST_NOTIFICATIONS" to false
        ),
        onRequestPermission = {},
        refresh = {}
    )
}