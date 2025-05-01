package esi.roadside.assistance.client

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import esi.roadside.assistance.client.core.util.NotificationsReceiver
import kotlin.apply
import kotlin.collections.forEach
import kotlin.let

class NotificationService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

    fun showNotification(
        id: Int,
        title: String,
        content: String,
        vararg actions: NotificationCompat.Action
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = intent?.let {
            TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(it)
                .getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID,
        )
            .setContentTitle(CHANNEL_NAME)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content)
                    .setBigContentTitle(title)
                    .setSummaryText(content)
            )
            .apply {
                actions.forEach { addAction(it) }
            }
            .build()
        notificationManager.notify(id, notification)
    }
    companion object {
        const val CHANNEL_ID = "esi.roadside.assistance.client"
        const val CHANNEL_NAME = "Roadside Assistance Client"
        const val CHANNEL_DESCRIPTION = "Roadside Assistance Client Notifications"
        const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    }
}