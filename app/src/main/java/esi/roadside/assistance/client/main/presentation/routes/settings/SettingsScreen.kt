package esi.roadside.assistance.client.main.presentation.routes.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.main.presentation.constants.Settings
import esi.roadside.assistance.client.settings.presentation.LargeSettingsGroup
import esi.roadside.assistance.client.settings.presentation.settingsLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val settings = Settings.groups
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.settings),
                background = R.drawable.union,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(bottom = 16.dp),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            settings.forEach {
                settingsLabel(it.label)
                item {
                    LargeSettingsGroup(it.list, navController)
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton({ onAction(Action.Logout) }) {
                        Text(stringResource(R.string.logout))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    PreviewAppTheme {
        SettingsScreen(rememberNavController(), {})
    }
}