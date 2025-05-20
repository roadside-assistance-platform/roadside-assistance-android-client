package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ProviderInfo

sealed interface ServiceAction {
    data class Submit(val request: AssistanceRequestModel, val onTimeOut: () -> Unit): ServiceAction
    data object Cancel: ServiceAction
    data class Accepted(val providerInfo: ProviderInfo): ServiceAction
    data object Arrived: ServiceAction
    data class SendMessage(val message: String): ServiceAction
    data class LocationUpdate(val location: LocationModel, val eta: Double?): ServiceAction
    data class SetDistance(val distance: Double): ServiceAction
    data object WorkFinished: ServiceAction
    data class Complete(val rating: Double?): ServiceAction
}