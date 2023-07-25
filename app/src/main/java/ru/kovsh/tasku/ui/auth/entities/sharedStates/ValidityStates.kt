package ru.kovsh.tasku.ui.auth.entities.sharedStates

sealed class ValidityStates {
    object None: ValidityStates()
    object Empty : ValidityStates()
    object Valid : ValidityStates()
    object Invalid : ValidityStates()
}

