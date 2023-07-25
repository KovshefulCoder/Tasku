package ru.kovsh.tasku.models.tasks.enities.resultStates

import ru.kovsh.tasku.models.areas.entities.resultStates.ResultState
import ru.kovsh.tasku.models.tasks.enities.Task

sealed class GetAllTasksResultState : ResultState {
    data class Success(val tasks: List<Task>) : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState()
    object Unauthorized : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState()
    object InternalServerError : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState()
    object Error : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState()
    object NoServer : GetAllTasksResultState()
    object None : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState()
}