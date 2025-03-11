package esi.roadside.assistance.client.main.presentation.routes.settings

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.main.presentation.constants.Settings.languages
import esi.roadside.assistance.client.settings.presentation.settingsRadioItems
import esi.roadside.assistance.client.settings.util.findActivity
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val dataStore = koinInject<SettingsDataStore>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val language by dataStore.language.collectAsState(initial = "system")
    var selectedLanguage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.language),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {  }) {
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
            settingsRadioItems(
                languages.toList(),
                languages.map { it.key }.indexOf(language),
                {
                    selectedLanguage = languages.keys.elementAt(it)
                    scope.launch {
                        dataStore.saveSettings(language = languages.keys.elementAt(it))
                        context.findActivity()?.runOnUiThread {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                context.getSystemService(LocaleManager::class.java)
                                    .applicationLocales =
                                    LocaleList.forLanguageTags(
                                        if (selectedLanguage == "system") {
                                            LocaleManagerCompat.getSystemLocales(context)[0]!!.language
                                        } else {
                                            selectedLanguage
                                        }
                                    )
                            } else {
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(selectedLanguage),
                                )
                            }
                        }
                    }
                },
            ) { Text(stringResource(it.second)) }
        }
    }
}