package ru.kovsh.tasku.ui.mainMenu.entities

import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState

data class SettingsStates(
    val isAuthorized: UserAuthenticationState = UserAuthenticationState.None
)
