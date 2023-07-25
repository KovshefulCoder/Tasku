package ru.kovsh.tasku.models.tasks.enities.resultStates

sealed class GetRemoteTasksResultState {
    object Success : GetRemoteTasksResultState()
    object Unauthorized : GetRemoteTasksResultState()
    object Error : GetRemoteTasksResultState()
    object NoServer : GetRemoteTasksResultState()
    object None : GetRemoteTasksResultState()
}