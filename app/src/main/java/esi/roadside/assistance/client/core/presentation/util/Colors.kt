package esi.roadside.assistance.client.core.presentation.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun navigationBarItemColors(): NavigationBarItemColors {
    val isFollowingSystemColors by isFollowingSystemColors()
    return if (isFollowingSystemColors) NavigationBarItemDefaults.colors()
    else NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.background,
        selectedTextColor = MaterialTheme.colorScheme.onBackground,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
