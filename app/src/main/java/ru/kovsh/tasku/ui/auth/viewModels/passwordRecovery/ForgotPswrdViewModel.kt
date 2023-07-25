package ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.passwordRecoveryStates.ForgotPswrdStates
import ru.kovsh.tasku.ui.auth.viewModels.Validations
import ru.kovsh.tasku.ui.auth.viewModels.Validations.validateEmail
import javax.inject.Inject

@HiltViewModel
class ForgotPswrdViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPswrdStates())
    val state: StateFlow<ForgotPswrdStates> = _state.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        viewModelScope.launch {
            val isValidEmail = validateEmail(newEmail)
            _state.update { value ->
                value.copy(
                    email = newEmail,
                    isValidEmail = isValidEmail
                )
            }
        }
    }

    fun onSendButtonClicked() {
        viewModelScope.launch {
            if (!Validations.approveValidation(_state.value.isValidEmail)) {
                _state.update { value -> value.copy(authResult = AuthRequestsStates.InvalidData) }
            } else {
                _state.update { value -> value.copy(authResult = AuthRequestsStates.Loading) }
                val result = repository.restorePassword(_state.value.email)
                requestStatesHandling(result)
            }
        }
    }

    fun onDialogBackClicked() {
        viewModelScope.launch {
            _state.update { value -> value.copy(authResult = AuthRequestsStates.None) }
        }
    }


    private fun requestStatesHandling(result: PasswordRecoveryResult<Unit>) {
        viewModelScope.launch {
            when (result) {
                is PasswordRecoveryResult.OK -> {
                    _state.update { value ->
                        value.copy(authResult = AuthRequestsStates.Success)
                    }
                }

                else -> {
                    _state.update { value ->
                        value.copy(authResult = AuthRequestsStates.Error)
                    }
                }
            }
        }
    }
}