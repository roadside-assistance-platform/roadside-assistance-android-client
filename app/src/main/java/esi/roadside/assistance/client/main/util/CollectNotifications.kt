package esi.roadside.assistance.client.main.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import esi.roadside.assistance.client.core.presentation.util.EventBus
import esi.roadside.assistance.client.main.domain.models.NotificationModel

@Composable
fun CollectNotifications(
    callback: (NotificationModel) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = lifecycleOwner) {
//        NotificationListener.listenForNotifications {
//
//        }
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            NotificationListener.notifications.collect(callback)
        }
    }
}