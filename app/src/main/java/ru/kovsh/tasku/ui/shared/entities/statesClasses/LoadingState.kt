package ru.kovsh.tasku.ui.shared.entities.statesClasses

import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState

data class MainSharedState(
    val authorizationStatus: UserAuthenticationState = UserAuthenticationState.None,
    val remoteState: LoadingState = LoadingState.Idle,
)

enum class LoadingState {
    Idle,
    Loading,
    Success,
    Error,
    Closed
}