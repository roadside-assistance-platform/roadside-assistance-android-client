package esi.roadside.assistance.client.core.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import esi.roadside.assistance.client.NotificationService

class NotificationsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = NotificationService(context!!)
    }
}