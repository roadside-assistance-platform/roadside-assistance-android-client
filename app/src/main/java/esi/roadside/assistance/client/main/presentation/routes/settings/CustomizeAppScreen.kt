package esi.roadside.assistance.client.main.presentation.routes.settings

import android.os.Build
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.DarkMode
import androidx.compose.material.icons.twotone.InvertColors
import androidx.compose.material.icons.twotone.SettingsSuggest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.main.presentation.constants.Settings.themeOptions
import esi.roadside.assistance.client.settings.presentation.checkSettingsItem
import esi.roadside.assistance.client.settings.presentation.dropdownSettingsItem
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeAppScreen(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val dataStore = koinInject<SettingsDataStore>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isDark by isDark()
    val dynamicColorsChecked by dataStore.dynamicColors.collectAsState(initial = true)
    val extraDarkChecked by dataStore.extraDark.collectAsState(initial = true)
    val theme by dataStore.theme.collectAsState(initial = "light")
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.customize_app),
                background = R.drawable.union,
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            dropdownSettingsItem(
                items = themeOptions,
                selected = themeOptions.first { it.first == theme },
                onSelectedItem = {
                    scope.launch {
                        dataStore.saveSettings(theme = it.first)
                    }
                },
                icon = Icons.TwoTone.InvertColors,
                title = R.string.app_theme,
                text = R.string.choose_app_theme,
                itemIcon = {
                    Icon(it.second.second, null)
                },
                itemText = {
                    Text(stringResource(it.second.first))
                }
            )
            checkSettingsItem(
                icon = Icons.TwoTone.DarkMode,
                title = R.string.extra_dark_colors,
                text = R.string.extra_dark_description,
                checked = extraDarkChecked,
                visible = isDark,
                onCheckedChange = { checked ->
                    scope.launch {
                        dataStore.saveSettings(extraDark = checked)
                    }
                },
            )
            checkSettingsItem(
                icon = Icons.TwoTone.SettingsSuggest,
                title = R.string.dynamic_colors,
                text = R.string.follow_system_dynamic_colors,
                checked = dynamicColorsChecked,
                visible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                onCheckedChange = { checked ->
                    scope.launch {
                        dataStore.saveSettings(dynamic = checked)
                    }
                },
            )
        }
    }
}