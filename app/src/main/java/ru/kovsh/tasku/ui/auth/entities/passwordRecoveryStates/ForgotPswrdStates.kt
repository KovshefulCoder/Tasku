package ru.kovsh.tasku.ui.auth.entities.passwordRecoveryStates

import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates

data class ForgotPswrdStates(
    val email: String = "",
    val isValidEmail: ValidityStates = ValidityStates.None,
    val authResult: AuthRequestsStates = AuthRequestsStates.None,
)
