package esi.roadside.assistance.client.main.util

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.PolymorphicNotification
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class QueuesManager() {
    private val _notifications = Channel<PolymorphicNotification>()
    private val _userNotifications = Channel<PolymorphicNotification.UserNotification>()
    private val _services = Channel<PolymorphicNotification.Service>()
    private val _serviceAcceptance = Channel<PolymorphicNotification.ServiceAcceptance>()
    val serviceAcceptance = _serviceAcceptance
    private val _locationUpdate = Channel<PolymorphicNotification.LocationUpdate>()
    val locationUpdate = _locationUpdate
    private val _providerArrival = Channel<PolymorphicNotification.ProviderArrived>()
    val providerArrival = _providerArrival
    var channel: com.rabbitmq.client.Channel? = null

    private fun connect() : com.rabbitmq.client.Channel {
        if (channel == null)
            channel = ConnectionFactory().apply {
                setUri(BuildConfig.CLOUDAMPQ_URL)
                useSslProtocol()
            }.newConnection().createChannel()
        return channel!!
    }

    fun close() {
        if (channel == null) return
        if (!channel!!.isOpen) return
        channel!!.close()
    }

    private fun consume(queueName: String, exchanges: List<String> = emptyList()) {
        val channel = connect()
        val queue = channel.queueDeclare(queueName, true, false, false, null).queue
        val consumer = DeliverCallback { _, delivery ->
            String(delivery.body, Charsets.UTF_8).let { message ->
                try {
                    println("Received message: $message")
                    val deserialized = Json.decodeFromString<PolymorphicNotification>(message)
                    println("Deserialized message: $deserialized")
                    _notifications.trySend(deserialized)
                    when(deserialized) {
                        is PolymorphicNotification.Service -> _services.trySend(deserialized)
                        is PolymorphicNotification.UserNotification -> _userNotifications.trySend(deserialized)
                        is PolymorphicNotification.ServiceAcceptance -> _serviceAcceptance.trySend(deserialized)
                        is PolymorphicNotification.ProviderArrived -> _providerArrival.trySend(deserialized)
                        else -> return@let
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            channel.basicAck(delivery.envelope.deliveryTag, false)
        }
        val cancelCallback = CancelCallback { consumerTag ->
            println("Consumer cancelled: $consumerTag")
        }
        if (exchanges.isNotEmpty())
            exchanges.forEach { exchange ->
                channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT)
                channel.queueBind(queue, exchange, "")
                channel.basicConsume(queue, false, consumer, cancelCallback)
            }
        else
            channel.basicConsume(queue, false, consumer, cancelCallback)
    }

    private fun publishToExchanges(exchanges: List<String>, message: PolymorphicNotification) {
        val channel = connect()
        exchanges.forEach { exchange ->
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT)
            channel.basicPublish(
                exchange,
                "",
                null,
                Json.encodeToString(PolymorphicNotification.serializer(), message).toByteArray()
            )
        }
    }

    private fun publishToQueue(queueName: String, message: PolymorphicNotification) {
        val channel = connect()
        channel.queueDeclare(queueName, true, false, false, null)
        channel.basicPublish(
            "",
            queueName,
            null,
            Json.encodeToString(PolymorphicNotification.serializer(), message).toByteArray()
        )
    }

    fun consumeUserNotifications(userId: String, type: String) = consume("notifications-$type-$userId")

    fun consumeCategoryQueues(categories: Set<Categories>) =
        consume("", categories.map {
            "${it.name.lowercase()}-notifications-exchange"
        })

    fun publishCategoryQueues(categories: Set<Categories>, message: PolymorphicNotification) =
        publishToExchanges(categories.map {
            "${it.name.lowercase()}-notifications-exchange"
        }, message)

    fun publishUserNotification(userId: String, type: String, message: PolymorphicNotification) =
        publishToQueue("notifications-$type-$userId", message)
}
