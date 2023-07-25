package ru.kovsh.tasku.ui.mainMenu.entities

import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState

data class MainMenuStates(
    val isAuthorized: UserAuthenticationState = UserAuthenticationState.None,
    val refreshRequestState: UserAuthenticationState = UserAuthenticationState.None,
    val visibleAreasIndexes: MutableMap<Int, Pair<Float, Float>> = mutableMapOf(),
    val newElementIndex: Int = -2
)
