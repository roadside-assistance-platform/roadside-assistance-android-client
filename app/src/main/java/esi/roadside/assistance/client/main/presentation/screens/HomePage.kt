package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.main.presentation.MainPaths

@Composable
fun HomePage(modifier: Modifier = Modifier,
             navHostController: NavHostController,
             onNotificationClick : () -> Unit,
             onFabClick: () -> Unit
             ) {
    Scaffold (
        topBar = {
            Row(Modifier.fillMaxWidth().padding(25.dp), horizontalArrangement = Arrangement.End) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = modifier.size(40.dp).clip(CircleShape).shadow(4.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Icon(Icons.Default.Notifications, null,Modifier.fillMaxSize(0.7f))
                }
            }
        },
        bottomBar = {
            val items = listOf(
                MainPaths.Home to Icons.Outlined.Home,
                MainPaths.Profile to Icons.Outlined.Person,
                MainPaths.Settings to Icons.Outlined.Settings
            )

            NavigationBar(
                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                tonalElevation = 0.dp,
                modifier = modifier.clip(RoundedCornerShape(80.dp))
            ) {
                items.forEach { (path, icon) ->
                    val isSelected = navHostController.currentDestination?.route ?: MainPaths.Home.name == path.name
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(path.name) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = path.name,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Use theme colors
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick
            ) {
                Text(stringResource(R.string.request_a_service), modifier.padding(start = 6.dp, end = 6.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){ PaddingValues ->
//        TODO("Add map ui here")
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme() {
        HomePage(navHostController = rememberNavController(), onNotificationClick = {}, onFabClick = {})
    }
}

