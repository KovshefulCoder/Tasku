package ru.kovsh.tasku.ui.auth.entities.sharedStates

sealed class AuthRequestsStates {
    object None : AuthRequestsStates()
    object InvalidData : AuthRequestsStates()
    object Loading : AuthRequestsStates()
    object LoadingEmailConfirmation : AuthRequestsStates()
    object Success : AuthRequestsStates()
    object Error : AuthRequestsStates()
}