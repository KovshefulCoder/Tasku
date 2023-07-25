package ru.kovsh.tasku.models.tasks.enities.resultStates

sealed class UpdateTaskResultState {
    object Success : UpdateTaskResultState()
    object BadRequest : UpdateTaskResultState()
    object Unauthorized : UpdateTaskResultState()
    object InternalServerError : UpdateTaskResultState()
    object Error : UpdateTaskResultState()
    object None : UpdateTaskResultState()
}
