package esi.roadside.assistance.client.main.presentation.routes.settings

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.main.presentation.constants.Settings.themeOptions
import esi.roadside.assistance.client.settings.presentation.checkSettingsItem
import esi.roadside.assistance.client.settings.presentation.settingsItem
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeAppScreen(modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val dataStore = koinInject<SettingsDataStore>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val isDark by dataStore.isDark().collectAsState(initial = false)
    val dynamicColorsChecked by dataStore.dynamicColors.collectAsState(initial = true)
    val extraDarkChecked by dataStore.extraDark.collectAsState(initial = true)
    val theme by dataStore.theme.collectAsState(initial = "system")
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.theme),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = paddingValues,
        ) {
            settingsItem(
                Icons.TwoTone.InvertColors,
                R.string.app_theme,
                R.string.choose_app_theme,
            )
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SingleChoiceSegmentedButtonRow {
                        themeOptions.forEachIndexed { index, pair ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = themeOptions.size),
                                onClick = {
                                    scope.launch {
                                        dataStore.saveSettings(theme = pair.first)
                                    }
                                },
                                selected = theme == pair.first,
                            ) {
                                Text(stringResource(id = pair.second))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
            }
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