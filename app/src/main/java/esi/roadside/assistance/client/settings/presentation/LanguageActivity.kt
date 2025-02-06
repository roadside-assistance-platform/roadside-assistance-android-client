package esi.roadside.assistance.client.settings.presentation

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.settings.util.findActivity
import org.koin.android.ext.android.inject

class LanguageActivity : ComponentActivity() {
    private val dataStore by inject<SettingsDataStore>()
    private val languages =
        mapOf(
            "system" to R.string.follow_system,
            "en" to R.string.english,
            "fr" to R.string.french,
            "ar" to R.string.arabic,
            "es" to R.string.spanish,
            "it" to R.string.italian,
            "in" to R.string.hindi,
        )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SetSystemBarColors(dataStore)
            val listState = rememberLazyListState()
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val language by dataStore.language.collectAsState(initial = "system")
            var selectedLanguage by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()
            AppTheme {
                Scaffold(
                    modifier =
                        Modifier
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
                                IconButton(onClick = { finish() }) {
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
                                    findActivity()?.runOnUiThread {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            getSystemService(LocaleManager::class.java)
                                                .applicationLocales =
                                                LocaleList.forLanguageTags(
                                                    if (selectedLanguage == "system") {
                                                        LocaleManagerCompat.getSystemLocales(this@LanguageActivity)[0]!!.language
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
        }
    }
}
