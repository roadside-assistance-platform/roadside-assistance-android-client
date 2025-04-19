package esi.roadside.assistance.client.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import esi.roadside.assistance.client.core.data.SettingsDataStore
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject


@Composable
fun isFollowingSystemColors() =
    koinInject<SettingsDataStore>().theme.map { it == "light" }.collectAsStateWithLifecycle(true)
