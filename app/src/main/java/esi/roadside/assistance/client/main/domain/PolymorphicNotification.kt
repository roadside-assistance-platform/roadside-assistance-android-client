package esi.roadside.assistance.client.main.domain

import esi.roadside.assistance.client.core.data.dto.Comment
import esi.roadside.assistance.client.main.domain.models.ClientInfo
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule

@Serializable(with = PolymorphicNotification.Serializer::class)
sealed interface PolymorphicNotification {
    @Serializable
    data class Service(
        val id: String = "",
        val client: ClientInfo = ClientInfo(),
        val provider: ProviderInfo? = null,
        val price: Int = 0,
        val description: String = "",
        val serviceRating: Float = 0f,
        val serviceLocation: String = "",
        val done: Boolean = false,
        val serviceCategory: Categories = Categories.OTHER,
        val createdAt: Long = 0,
        val updatedAt: Long = 0,
        val comments: List<Comment> = emptyList(),
    ) : PolymorphicNotification

    @Serializable
    data class UserNotification(
        val id: String,
        val title: String,
        val text: String,
        val isWarning: Boolean,
        val image: String?,
        val createdAt: String
    ) : PolymorphicNotification

    @Serializable
    data class LocationUpdate(
        val longitude: Double,
        val latitude: Double,
        val eta: Double?
    ) : PolymorphicNotification

    @Serializable
    data class ServiceAcceptance(
        val id: String,
        val category: Categories,
        val provider: ProviderInfo,
    ) : PolymorphicNotification

    @Serializable
    data object ProviderArrived : PolymorphicNotification

    @Serializable
    data class Message(val content: String) : PolymorphicNotification

    @Serializable
    data class ServiceDone(
        val price: Int,
        val rating: Double?
    ) : PolymorphicNotification

    @Serializable
    data class ServiceRemove(
        val serviceId: String,
        val exception: String?
    ) : PolymorphicNotification


    object Serializer : KSerializer<PolymorphicNotification> {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            allowStructuredMapKeys = true
            prettyPrint = true
            coerceInputValues = true
            classDiscriminator = "polymorphicType"
            serializersModule = SerializersModule {
                polymorphic(PolymorphicNotification::class, Service::class, Service.serializer())
                polymorphic(PolymorphicNotification::class, UserNotification::class, UserNotification.serializer())
                polymorphic(PolymorphicNotification::class, LocationUpdate::class, LocationUpdate.serializer())
                polymorphic(PolymorphicNotification::class, ServiceAcceptance::class, ServiceAcceptance.serializer())
                polymorphic(PolymorphicNotification::class, Message::class, Message.serializer())
                polymorphic(PolymorphicNotification::class, Message::class, Message.serializer())
                polymorphic(PolymorphicNotification::class, ServiceDone::class, ServiceDone.serializer())
                polymorphic(PolymorphicNotification::class, ProviderArrived::class, ProviderArrived.serializer())
                polymorphic(PolymorphicNotification::class, ServiceRemove::class, ServiceRemove.serializer())
            }
        }

        override val descriptor: SerialDescriptor
            get() = PolymorphicSerializer(PolymorphicNotification::class).descriptor
        override fun serialize(encoder: Encoder, value: PolymorphicNotification) {
            val polymorphicSerializer = PolymorphicSerializer(PolymorphicNotification::class)
            val jsonString = json.encodeToString(polymorphicSerializer, value)
            val element = json.parseToJsonElement(jsonString)
            (encoder as JsonEncoder).encodeJsonElement(element)
        }

        override fun deserialize(decoder: Decoder): PolymorphicNotification {
            val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
            return when (
                val itemType = jsonElement.jsonObject["polymorphicType"]?.jsonPrimitive?.content?.substringAfterLast(".")
            ) {
                "Service" -> json.decodeFromJsonElement(Service.serializer(), jsonElement)
                "UserNotification" -> json.decodeFromJsonElement(UserNotification.serializer(), jsonElement)
                "LocationUpdate" -> json.decodeFromJsonElement(LocationUpdate.serializer(), jsonElement)
                "ServiceAcceptance" -> json.decodeFromJsonElement(ServiceAcceptance.serializer(), jsonElement)
                "Message" -> json.decodeFromJsonElement(Message.serializer(), jsonElement)
                "ServiceDone" -> json.decodeFromJsonElement(ServiceDone.serializer(), jsonElement)
                "ProviderArrived" -> json.decodeFromJsonElement(ProviderArrived.serializer(), jsonElement)
                "ServiceRemove" -> json.decodeFromJsonElement(ServiceRemove.serializer(), jsonElement)
                else -> throw SerializationException("Unknown itemType: $itemType")
            }
        }
    }
}