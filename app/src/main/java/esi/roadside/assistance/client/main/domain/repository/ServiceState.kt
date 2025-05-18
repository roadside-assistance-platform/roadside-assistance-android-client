package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.main.domain.PolymorphicNotification
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.presentation.ClientState

data class ServiceState(
    val clientState: ClientState = ClientState.IDLE,
    val serviceModel: ServiceModel? = null,
    val providerLocation: LocationModel? = null,
    val acceptance: PolymorphicNotification.ServiceAcceptance? = null,
    val providerInfo: ProviderInfo? = null,
    val time: Long = 0L,
    val eta: Double? = null,
    val price: Int = 0,
)