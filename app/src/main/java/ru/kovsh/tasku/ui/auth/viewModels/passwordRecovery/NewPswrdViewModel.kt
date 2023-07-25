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
import ru.kovsh.tasku.ui.auth.entities.passwordRecoveryStates.NewPswrdStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.viewModels.Validations
import javax.inject.Inject

@HiltViewModel
class NewPswrdViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewPswrdStates())
    val state: StateFlow<NewPswrdStates> = _state.asStateFlow()

    fun onPasswordChange(newPassword: String) {
        viewModelScope.launch {
            _state.update { value ->
                value.copy(
                    password = newPassword,
                    isValidPassword = Validations.validatePassword(newPassword)
                )
            }
        }
    }

    fun onRepeatedPasswordChange(newRepeatedPassword: String) {
        viewModelScope.launch {
            _state.update { value ->
                value.copy(
                    repeatedPassword = newRepeatedPassword,
                    isValidRepeatedPassword = Validations.validateRepeatedPassword(
                        _state.value.password,
                        newRepeatedPassword
                    )
                )
            }
        }
    }

    fun onPasswordSubmit() {
        viewModelScope.launch {
            if (!Validations.approveValidation(
                    _state.value.isValidPassword, _state.value.isValidRepeatedPassword)) {
                _state.update { value -> value.copy(authResult = AuthRequestsStates.InvalidData) }
            } else {
                _state.update { value -> value.copy(authResult = AuthRequestsStates.Loading) }
                val result = repository.newPassword(_state.value.password, _state.value.token)
                requestStatesHandling(result)
            }
        }
    }

    private fun requestStatesHandling(result: PasswordRecoveryResult<Unit>) {
        viewModelScope.launch {
            when (result) {
                is PasswordRecoveryResult.OK -> {
                    _state.update { value ->
                        value.copy(authResult = AuthRequestsStates.Success)
                    } }
                else -> {
                    _state.update { value ->
                        value.copy(authResult = AuthRequestsStates.Error)
                    }
                }
            }
        }
    }

    fun onSaveToken(newToken: String) {
        viewModelScope.launch {
            _state.update { value ->
                value.copy(
                    token = newToken
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}