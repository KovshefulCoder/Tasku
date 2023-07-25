package ru.kovsh.tasku.ui.auth.entities.passwordRecoveryStates

import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates

data class NewPswrdStates(
    val token: String = "",
    val password: String = "",
    val isValidPassword: ValidityStates = ValidityStates.None,
    val repeatedPassword: String = "",
    val isValidRepeatedPassword: ValidityStates = ValidityStates.None,
    val authResult: AuthRequestsStates = AuthRequestsStates.None,
)
