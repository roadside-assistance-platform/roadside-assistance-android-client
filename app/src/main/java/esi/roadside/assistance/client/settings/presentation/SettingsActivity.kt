package esi.roadside.assistance.client.settings.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Palette
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.settings.data.languages
import org.koin.android.ext.android.inject
import kotlin.jvm.java

class SettingsActivity : ComponentActivity() {
    val settingsDataStore by inject<SettingsDataStore>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SetSystemBarColors(settingsDataStore)
            val language by settingsDataStore.language.collectAsState(initial = "system")
            val themeSettings = listOf(
                LargeSettingsItem(
                    R.string.customize_app,
                    R.string.customize_app,
                    Icons.TwoTone.Palette
                ) {
                    startActivity(Intent(this, ThemeActivity::class.java))
                }
            )
            val globalSettings = listOf(
                LargeSettingsItem(
                    R.string.language,
                    languages.getOrDefault(language, R.string.english),
                    Icons.TwoTone.Language
                ) {
                    startActivity(
                        Intent(this, LanguageActivity::class.java)
                    )
                },
                LargeSettingsItem(
                    R.string.about,
                    R.string.about_description,
                    Icons.TwoTone.Info
                ) {
                    startActivity(
                        Intent(this, AboutActivity::class.java)
                    )
                }
            )

            val scrollBehavior =
                TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val listState = rememberLazyListState()
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
                                    stringResource(id = R.string.settings),
                                    maxLines = 1,
                                    overflow = TextOverflow.Companion.Ellipsis,
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
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = listState,
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            LargeSettingsGroup(themeSettings)
                        }
                        item {
                            LargeSettingsGroup(globalSettings)
                        }
                    }
                }
            }
        }
    }
}
