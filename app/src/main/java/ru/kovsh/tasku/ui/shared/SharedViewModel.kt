package ru.kovsh.tasku.ui.shared

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.models.mixedRepo.SharedRepository
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetRemoteTasksResultState
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState
import ru.kovsh.tasku.ui.shared.entities.statesClasses.MainSharedState
import ru.kovsh.tasku.ui.shared.entities.statesClasses.LoadingState
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val sharedRepository: SharedRepository,
    private val authRepository: AuthRepository,
    private val pref: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(MainSharedState())
    val state = _state.asStateFlow()

    fun reset() {
        viewModelScope.cancel()
        _state.update { MainSharedState() }
    }

    private val _curAreas : MutableList<Area> = mutableStateListOf()
    val curAreas get() = _curAreas


    fun resetRemoteState() {
        _state.update { it.copy(remoteState = LoadingState.Idle) }
    }

    fun downloadAllTasks() {
        if (_state.value.remoteState != LoadingState.Idle) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(remoteState = LoadingState.Loading) }
            when (sharedRepository.getRemoteTasks()) {
                is GetRemoteTasksResultState.Success -> {
                    Log.i("downloadAllTasks", "Success")
                    _state.update {
                        it.copy(
                            remoteState = LoadingState.Success,
                            authorizationStatus = UserAuthenticationState.Authorized
                        )
                    }
                    delay(5000)
                    _state.update { it.copy(remoteState = LoadingState.Idle) }
                }
                is GetRemoteTasksResultState.Unauthorized -> {
                    Log.i("downloadAllTasks", "Unauthorized")
                    val refreshToken = pref.getString("refresh", "")
                    if (refreshToken != null) {
                        when(authRepository.refresh(refreshToken)) {
                            is AuthResult.Authorized -> {
                                downloadAllTasks()
                            }
                            else -> {
                                _state.update {
                                    it.copy(
                                        remoteState = LoadingState.Error,
                                        authorizationStatus = UserAuthenticationState.Unauthorized
                                    )
                                }
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                remoteState = LoadingState.Error,
                                authorizationStatus = UserAuthenticationState.Unauthorized
                            )
                        }
                    }
                }
                is GetRemoteTasksResultState.NoServer -> {
                    Log.i("downloadAllTasks", "NoServer")
                    _state.update {
                        it.copy(
                            remoteState = LoadingState.Error,
                            authorizationStatus = UserAuthenticationState.NoServer
                        )
                    }
                }
                else -> {
                    Log.i("downloadAllTasks", "Error")
                    _state.update {
                        it.copy(
                            remoteState = LoadingState.Error,
                            authorizationStatus = UserAuthenticationState.Unauthorized
                        )
                    }
                }
            }
        }
    }

    fun getLocalAreas() {
        _curAreas.clear()
        viewModelScope.launch {
            _curAreas.addAll(sharedRepository.getLocalAreas())
        }
    }
}