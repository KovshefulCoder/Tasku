package ru.kovsh.tasku.ui.auth.entities.signInUpStates

sealed class AuthScreenStates {
    object None : AuthScreenStates()
    object SignIn : AuthScreenStates()
    object SignUp : AuthScreenStates()
    object Authentication : AuthScreenStates()
    object ForgotPassword : AuthScreenStates()
}