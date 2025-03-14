package esi.roadside.assistance.client.core.util.composables

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import esi.roadside.assistance.client.core.presentation.util.isDark

@Composable
fun ComponentActivity.SetSystemBarColors() {
    val isDark by isDark()
    val scrim = MaterialTheme.colorScheme.scrim
    DisposableEffect(isDark) {
        val statusBarStyle = SystemBarStyle.dark(scrim.copy(alpha = .25f).toArgb())
        val navigationBarStyle =
            if (isDark) SystemBarStyle.dark(Color.TRANSPARENT)
            else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(
            statusBarStyle = statusBarStyle,
            navigationBarStyle = navigationBarStyle,
        )
        onDispose { }
    }
}
