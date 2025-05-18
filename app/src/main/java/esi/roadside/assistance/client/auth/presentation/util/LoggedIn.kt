package esi.roadside.assistance.client.auth.presentation.util

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.util.account.AccountManager
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.presentation.util.Event.LaunchMainActivity
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.launch

fun ViewModel.loggedIn(
    accountManager: AccountManager,
    client: ClientModel? = null,
    launchMainActivity: Boolean = true
) {
    Log.i("Welcome", "Logged in successfully: $client")
    client?.let {
        viewModelScope.launch {
            accountManager.updateUser(client)
        }
    }
    if (launchMainActivity) sendEvent(LaunchMainActivity)
}
