package esi.roadside.assistance.client.main.util

import com.rabbitmq.client.*
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

object NotificationListener {
    private val _notifications = Channel<NotificationModel>()
    val notifications = _notifications.receiveAsFlow()

    fun listenForNotifications(userId: String, callback: (NotificationModel) -> Unit) {
        val factory = ConnectionFactory()
        factory.setUri(BuildConfig.CLOUDAMPQ_URL)

        CoroutineScope(Dispatchers.IO).launch {
            val connection = factory.newConnection()
            val channel = connection.createChannel()

            val queueName = "notifications_$userId"
            channel.queueDeclare(queueName, false, false, false, null)

            println("Waiting for notifications...")

            val consumer = object : DefaultConsumer(channel) {
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray?
                ) {
                    val message = body?.let { String(it) }
                    println("Received notification: $message")
                    message?.let {
                        callback(it.toNotificationModel(LocalDateTime.now()))
                    }
                }
            }

            channel.basicConsume(queueName, true, consumer)
        }
    }
}