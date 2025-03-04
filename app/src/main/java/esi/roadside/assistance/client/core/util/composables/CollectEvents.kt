package esi.roadside.assistance.client.core.util.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.EventBus
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectEvents(
    callback: (Event) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            EventBus.events.collect(callback)
        }
    }
}