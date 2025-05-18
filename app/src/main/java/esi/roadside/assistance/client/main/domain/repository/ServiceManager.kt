package esi.roadside.assistance.client.main.domain.repository

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import esi.roadside.assistance.client.NotificationService
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.util.account.AccountManager
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.ShowMainActivityMessage
import esi.roadside.assistance.client.core.presentation.util.EventBus.sendEvent
import esi.roadside.assistance.client.main.domain.PolymorphicNotification
import esi.roadside.assistance.client.main.domain.PolymorphicNotification.Service
import esi.roadside.assistance.client.main.domain.PolymorphicNotification.ServiceDone
import esi.roadside.assistance.client.main.domain.PolymorphicNotification.ServiceRemove
import esi.roadside.assistance.client.main.domain.use_cases.FinishRequest
import esi.roadside.assistance.client.main.domain.use_cases.Rating
import esi.roadside.assistance.client.main.domain.use_cases.SubmitRequest
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.util.QueuesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ServiceManager(
    private val context: Context,
    accountManager: AccountManager,
    private val submitRequestUseCase: SubmitRequest,
    private val ratingUseCase: Rating,
    private val queuesManager: QueuesManager,
    private val notificationService: NotificationService,
    private val finishRequestUseCase: FinishRequest,
) {
    private val _service = MutableStateFlow<ServiceState>(ServiceState())
    val service = _service.asStateFlow()

    private val clientInfo = accountManager.getUserFlow().map { it.toClientInfo() }

    private var timer: CountDownTimer? = null

    suspend fun onAction(action: ServiceAction) {
        when(action) {
            is ServiceAction.Accepted -> {
                if (_service.value.clientState == ClientState.ASSISTANCE_REQUESTED) {
                    _service.update {
                        it.copy(
                            providerInfo = action.providerInfo,
                            clientState = ClientState.PROVIDER_IN_WAY
                        )
                    }
                    notificationService.showNotification(
                        0,
                        context.getString(R.string.assistance_request_accepted),
                        context.getString(
                            R.string.service_accepted_by,
                            action.providerInfo.fullName
                        ),
                    )
                    queuesManager.publishCategoryQueues(
                        service.value.serviceModel?.category?.let { setOf(it) } ?: emptySet(),
                        ServiceRemove(
                            serviceId = service.value.serviceModel?.id ?: "",
                            exception = action.providerInfo.id
                        )
                    )
                }
            }
            is ServiceAction.Submit -> {
                submitRequestUseCase(action.request).onSuccess { serviceModel ->
                    sendEvent(ShowMainActivityMessage(R.string.request_submitted))
                    _service.update {
                        it.copy(
                            clientState = ClientState.ASSISTANCE_REQUESTED,
                            serviceModel = serviceModel
                        )
                    }
                    withContext(Dispatchers.Main) {
                        timer?.cancel()
                        timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                _service.update {
                                    it.copy(time = millisUntilFinished)
                                }
                            }

                            override fun onFinish() {
                                action.onTimeOut()
                            }
                        }.start()
                    }
                    Log.i("ServiceManager", "category: ${action.request.serviceCategory}")
                    queuesManager.publishCategoryQueues(
                        setOf(action.request.serviceCategory),
                        Service(
                            id = serviceModel.id,
                            client = clientInfo.first(),
                            description = action.request.description,
                            serviceCategory = action.request.serviceCategory,
                            serviceLocation = action.request.serviceLocation.toString(),
                            provider = null,
                            price = 0
                        )
                    )
                }.onError {
                    sendEvent(ShowMainActivityMessage(it.text))
                }
            }
            ServiceAction.Cancel -> {
                timer?.cancel()
                _service.update {
                    it.copy(clientState = ClientState.IDLE, serviceModel = null)
                }
                _service.value.serviceModel?.let { serviceModel ->
                    queuesManager.publishCategoryQueues(
                        setOf(serviceModel.category),
                        ServiceRemove(
                            serviceId = serviceModel.id,
                            exception = null
                        )
                    )
                }
            }

            is ServiceAction.LocationUpdate -> {
                Log.i("ServiceManager", "Provider location: ${action.location}")
                _service.update {
                    it.copy(providerLocation = action.location, eta = action.eta)
                }
            }

            ServiceAction.WorkFinished -> {
                finishRequestUseCase(service.value.serviceModel?.id!!)
                    .onSuccess { result ->
                        _service.update {
                            it.copy(
                                clientState = ClientState.ASSISTANCE_COMPLETED,
                                price = result.price,
                                serviceModel = null
                            )
                        }
                    }
            }

            is ServiceAction.Complete -> {
                ratingUseCase(service.value.serviceModel?.id!!, action.rating)
                    .onSuccess { result ->
                        _service.update {
                            it.copy(
                                clientState = ClientState.IDLE,
                                providerLocation = null,
                                serviceModel = null,
                            )
                        }
                    }.onError {
                        sendEvent(ShowMainActivityMessage(it.text))
                    }
                queuesManager.publishUserNotification(
                    _service.value.providerInfo?.id ?: "",
                    "provider",
                    ServiceDone(
                        price = _service.value.price,
                        rating = action.rating
                    )
                )
            }
            ServiceAction.Arrived -> {
                _service.update {
                    it.copy(clientState = ClientState.ASSISTANCE_IN_PROGRESS)
                }
//                notificationService.showNotification(
//                    0,
//                    context.getString(R.string.provider_arrived),
//                    context.getString(R.string.provider_arrived_notification),
//                )
            }

            is ServiceAction.SendMessage -> {
                queuesManager.publishUserNotification(
                    _service.value.providerInfo?.id ?: "",
                    "provider",
                    PolymorphicNotification.Message(content = action.message)
                )
            }
        }
    }
}