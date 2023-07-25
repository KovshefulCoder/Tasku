package ru.kovsh.tasku.models.tasks.enities.resultStates

sealed class AddTaskResultState {
    data class Success(val taskID: Long) : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object BadRequest : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object Unauthorized : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object Forbidden : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object InternalServerError : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object Error : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
    object None : ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState()
}