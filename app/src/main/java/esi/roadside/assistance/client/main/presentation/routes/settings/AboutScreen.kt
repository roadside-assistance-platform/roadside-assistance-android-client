package esi.roadside.assistance.client.main.presentation.routes.settings

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.util.getAppVersion
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.main.presentation.constants.Settings
import esi.roadside.assistance.client.settings.presentation.AppIcon
import younesbouhouche.musicplayer.settings.presentation.AboutCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.about),
                background = R.drawable.union,
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState,
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            item {
                AppIcon()
            }
            item {
                Text(
                    stringResource(R.string.app_name),
                    Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                Button(onClick = {}) {
                    Text("v${context.getAppVersion()}")
                }
            }
            Settings
                .Developers
                .entries
                .sortedBy { it.type.ordinal }
                .groupBy { it.type }
                .forEach { (type, devs) ->
                item {
                    HorizontalDivider(Modifier.fillMaxWidth())
                }
                items(devs) { dev ->
                    DeveloperCard(dev)
                }
            }
            item {
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            item {
                AboutCard(
                    stringResource(R.string.project),
                    stringResource(R.string.source_code),
                    Icons.Default.Code,
                ) {
                }
            }
            item {
                AboutCard(
                    stringResource(R.string.send_feedback),
                    "y.bouhouche@esi-sba.dz",
                    Icons.Default.Mail,
                ) {
                    with(
                        Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:".toUri()
                            putExtra(
                                Intent.EXTRA_EMAIL,
                                arrayOf("younes.bouhouche12@gmail.com"),
                            )
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Feedback about Roadside Assistance Client App",
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "\nApp Version:${context.getAppVersion()}," +
                                        "\nAPI Level:${Build.VERSION.SDK_INT}",
                            )
                        },
                    ) {
                        if (this.resolveActivity(context.packageManager) != null) {
                            context.startActivity(this)
                        }
                    }
                }
            }
            item {
                AboutCard(
                    stringResource(R.string.translation),
                    stringResource(R.string.contribute_in_app_translation),
                    Icons.Default.Translate,
                ) {
                }
            }
        }
    }
}