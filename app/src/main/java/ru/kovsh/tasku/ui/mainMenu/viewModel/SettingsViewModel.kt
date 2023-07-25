package ru.kovsh.tasku.ui.mainMenu.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState
import ru.kovsh.tasku.ui.mainMenu.entities.SettingsStates
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pref: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsStates())
    val state = _state.asStateFlow()

    fun reset() {
        viewModelScope.cancel()
        _state.update {SettingsStates()}
    }

    fun onLogOut() {
        viewModelScope.launch {
            when (authRepository.logOut(pref.getString("refresh", "") ?: "")) {
                is AuthResult.Unauthorized -> {
                    _state.update {
                        it.copy(isAuthorized = UserAuthenticationState.Unauthorized)
                    }
                }
                else -> {
                   //TODO: add log out logic
                }
            }
        }
    }
}