package ru.kovsh.tasku.models.areas.entities.resultStates

interface ResultState {
    object Unauthorized
    object InternalServerError
    object Error
}