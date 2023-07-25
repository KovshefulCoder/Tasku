package ru.kovsh.tasku.ui.auth.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates
import ru.kovsh.tasku.ui.auth.entities.signInUpStates.AuthScreenStates
import ru.kovsh.tasku.ui.auth.entities.signInUpStates.AuthStates
import ru.kovsh.tasku.ui.auth.viewModels.Validations.approveValidation
import ru.kovsh.tasku.ui.auth.viewModels.Validations.validateEmail
import ru.kovsh.tasku.ui.auth.viewModels.Validations.validatePassword
import ru.kovsh.tasku.ui.auth.viewModels.Validations.validateRepeatedPassword
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthStates())
    val state: StateFlow<AuthStates> = _state.asStateFlow()

    fun onClearViewModel() {
        viewModelScope.launch {
            _state.update {
                AuthStates()
            }
            _state.update {
                it.copy(authScreenState = AuthScreenStates.None)
            }
        }
    }

    fun onForgotPasswordClicked() {
        _state.update { value ->
            value.copy(
                authScreenState = AuthScreenStates.ForgotPassword,
            )
        }
    }

    fun onAuthenticate(refreshToken: String) {
        viewModelScope.launch {
            if (_state.value.refreshToken == "" &&
                _state.value.authScreenState != AuthScreenStates.None)
            {
                _state.update { value ->
                    value.copy(
                        refreshToken = refreshToken,
                        authResult = AuthRequestsStates.LoadingEmailConfirmation,
                        authScreenState = AuthScreenStates.Authentication
                    )
                }
                val result = repository.authenticate(refreshToken)
                requestStatesHandling(result)
            }
        }
    }

    fun onEmailChanged(newEmail: String) {
        val isValidEmail = if (_state.value.authScreenState == AuthScreenStates.SignUp) {
            validateEmail(newEmail)
        } else {
            ValidityStates.Valid
        }
        _state.update { value ->
            value.copy(
                email = newEmail,
                isValidEmail = isValidEmail
            )
        }
    }

    fun onPasswordChanged(newPassword: String) {
        var isValidPassword: ValidityStates = ValidityStates.Valid
        var isValidRepeatedPassword: ValidityStates = ValidityStates.Valid
        if (_state.value.authScreenState == AuthScreenStates.SignUp) {
            isValidPassword = validatePassword(newPassword)
            if (_state.value.repeatedPassword != "") {
                // Check validity of repeated password if user
                // already inputted it after change of password
                isValidRepeatedPassword =
                    validateRepeatedPassword(newPassword, _state.value.repeatedPassword)
            }
        }
        _state.update { value ->
            value.copy(
                password = newPassword,
                isValidPassword = isValidPassword,
                isValidRepeatedPassword = isValidRepeatedPassword
            )
        }
    }

    fun onRepeatedPasswordChanged(newRepeatedPassword: String) {
        val isValidRepeatedPassword =
            if (_state.value.authScreenState == AuthScreenStates.SignUp) {
                validateRepeatedPassword(_state.value.password, newRepeatedPassword)
            } else {
                ValidityStates.Valid
            }
        _state.update { value ->
            value.copy(
                repeatedPassword = newRepeatedPassword,
                isValidRepeatedPassword = isValidRepeatedPassword
            )
        }
    }

    fun onBottomButtonClicked() {
        viewModelScope.launch {
            when (_state.value.authScreenState) {
                is AuthScreenStates.SignIn -> {
                    _state.update { value ->
                        value.copy(
                            authScreenState = AuthScreenStates.SignUp,
                            buttonTextID = R.string.sign_up_button,
                            bottomButtonDescriptionID = R.string.bottom_button_dscription_sign_up,
                            bottomButtonTextID = R.string.sign_in_button
                        )
                    }
                }

                is AuthScreenStates.SignUp -> {
                    _state.update { value ->
                        value.copy(
                            authScreenState = AuthScreenStates.SignIn,
                            buttonTextID = R.string.sign_in_button,
                            bottomButtonDescriptionID = R.string.bottom_button_dscription_sign_in,
                            bottomButtonTextID = R.string.sign_up_button
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun onAuthButtonClicked() {
        viewModelScope.launch {
            if (!approveValidation(
                    _state.value.isValidEmail,
                    _state.value.isValidPassword,
                    _state.value.isValidRepeatedPassword
                ) && _state.value.authScreenState == AuthScreenStates.SignUp
            ) {
                _state.update { value -> value.copy(authResult = AuthRequestsStates.InvalidData) }
            } else {
                Log.i("onAuthButtonClicked", "authScreenState: ${_state.value.authScreenState}")
                _state.update { value -> value.copy(authResult = AuthRequestsStates.Loading) }
                when (_state.value.authScreenState) {
                    AuthScreenStates.SignIn -> {
                        val result =
                            repository.signIn(_state.value.email, _state.value.password.trim())
                        requestStatesHandling(result)
                    }

                    AuthScreenStates.SignUp -> {
                        val result =
                            repository.signUp(_state.value.email, _state.value.password.trim())
                        requestStatesHandling(result)
                    }
                    else -> {}
                }
            }
        }
    }

    fun onDialogBackClicked() = _state.update {
            value -> value.copy(authResult = AuthRequestsStates.None)
    }

    private fun requestStatesHandling(result: AuthResult<Unit>) {
        viewModelScope.launch {
            when (result) {
                is AuthResult.Authorized -> {
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