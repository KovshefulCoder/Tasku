package ru.kovsh.tasku.ui.mainMenuCategory.entities

import ru.kovsh.tasku.ui.area.entities.NavigationStates
import ru.kovsh.tasku.ui.shared.entities.statesClasses.LoadingState

data class CategoryStates(
    val loadingStatus: LoadingState = LoadingState.Idle,
    val navigationState: NavigationStates = NavigationStates.None
)
