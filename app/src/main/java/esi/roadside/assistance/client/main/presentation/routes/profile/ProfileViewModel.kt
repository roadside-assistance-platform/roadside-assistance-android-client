package esi.roadside.assistance.client.main.presentation.routes.profile

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.ShowMainActivityMessage
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val accountManager: AccountManager,
    private val updateUseCase: Update,
    private val cloudinary: Cloudinary,
): ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.ConfirmProfileEditing -> {
                ValidateInput.validateUpdateProfile(
                    _state.value.editClient.fullName,
                    _state.value.editClient.email,
                    _state.value.editClient.phone
                )?.let { error ->
                    _state.update {
                        it.copy(
                            fullNameError = error.takeIf { it.field == Field.FULL_NAME },
                            emailError = error.takeIf { it.field == Field.EMAIL },
                            phoneError = error.takeIf { it.field == Field.PHONE_NUMBER }
                        )
                    }
                    return
                }
                _state.update { it.copy(loading = true) }
                viewModelScope.launch(Dispatchers.IO) {
                    cloudinary(
                        _state.value.editClient.photo ?: "".toUri(),
                        onSuccess = { url ->
                            _state.update {
                                it.copy(
                                    photo = url,
                                )
                            }
                        },
                        onFailure = {
                            sendEvent(ShowMainActivityMessage(R.string.error))
                        },
                        onFinished = {
                            viewModelScope.launch {
                                updateUseCase(_state.value.editClient.toUpdateModel().copy(
                                    photo = _state.value.photo
                                ))
                                    .onSuccess {
                                        accountManager.updateUser(it)
                                    }
                                    .onError {
                                        sendEvent(ShowMainActivityMessage(it.text))
                                    }

                                _state.update {
                                    it.copy(
                                        enableEditing = false,
                                        fullNameError = null,
                                        emailError = null,
                                        phoneError = null
                                    )
                                }
                                _state.update { it.copy(loading = false) }
                            }
                        },
                        onProgress = {}
                    )
                }
            }
            ProfileAction.EnableProfileEditing -> {
                _state.update {
                    it.copy(enableEditing = true)
                }
            }
            ProfileAction.CancelProfileEditing -> {
                _state.update {
                    it.copy(enableEditing = false, editClient = it.client)
                }
            }
            is ProfileAction.EditClient -> {
                _state.update {
                    it.copy(editClient = action.client)
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.getUserFlow().collectLatest { client ->
                _state.update {
                    it.copy(
                        client = client.toClientModel().toClientUi(),
                        editClient = client.toClientModel().toClientUi(),
                        photo = client.photo ?: ""
                    )
                }
            }
        }
    }
}