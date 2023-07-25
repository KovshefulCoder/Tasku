package ru.kovsh.tasku.ui.area.entities


data class AreaScreenStates(
    val navigationState: NavigationStates = NavigationStates.None,
    val newTaskText: String = ""
)
