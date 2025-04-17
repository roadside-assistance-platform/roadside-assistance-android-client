package esi.roadside.assistance.client.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.mapbox.maps.extension.style.expressions.dsl.generated.color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.rememberDynamicColorScheme
import esi.roadside.assistance.client.core.data.SettingsDataStore


@Composable
internal fun AppTheme(
    seedColor: Color? = null,
    content: @Composable() () -> Unit,
) {
    val datastore = SettingsDataStore(LocalContext.current)
    val theme by datastore.theme.collectAsState(initial = "light")
    val extraDark by datastore.extraDark.collectAsState(initial = false)
    val isDark =
        when (theme) {
            "light" -> false
            "dark" -> true
            else -> isSystemInDarkTheme()
        }
    val dynamicColor = datastore.dynamicColors.collectAsState(initial = false).value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val lightColors = lightScheme
    val darkColors = darkScheme
    val colorScheme =
        when {
            seedColor != null -> rememberDynamicColorScheme(seedColor = seedColor, isDark, extraDark)
            dynamicColor && isDark -> {
                dynamicDarkColorScheme(LocalContext.current)
            }
            dynamicColor && !isDark -> {
                dynamicLightColorScheme(LocalContext.current)
            }
            isDark -> darkColors
            else -> lightColors
        }
    DynamicMaterialTheme(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        neutral = Color(49, 49, 49),
        animate = true,
        useDarkTheme = isDark,
        withAmoled = extraDark,
        typography = interTypography(MaterialTheme.typography),
        content = content
    )
}


@Composable
internal fun PreviewAppTheme(
    seedColor: Color? = null,
    content: @Composable() () -> Unit,
) {
    val datastore = SettingsDataStore(LocalContext.current)
    val theme by datastore.theme.collectAsState(initial = "light")
    val extraDark by datastore.extraDark.collectAsState(initial = false)
    val isDark =
        when (theme) {
            "light" -> false
            "dark" -> true
            else -> isSystemInDarkTheme()
        }
    val dynamicColor = datastore.dynamicColors.collectAsState(initial = false).value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val lightColors = lightScheme
    val darkColors = darkScheme
    val colorScheme =
        when {
            seedColor != null -> rememberDynamicColorScheme(seedColor = seedColor, isDark, extraDark)
            dynamicColor && isDark -> {
                dynamicDarkColorScheme(LocalContext.current)
            }
            dynamicColor && !isDark -> {
                dynamicLightColorScheme(LocalContext.current)
            }
            isDark -> darkColors
            else -> lightColors
        }
    DynamicMaterialTheme(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        animate = true,
        withAmoled = extraDark,
        content = content
    )
}
