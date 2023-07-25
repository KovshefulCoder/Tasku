package ru.kovsh.tasku.models.tasks.enities.resultStates

import ru.kovsh.tasku.models.areas.entities.resultStates.ResultState
import ru.kovsh.tasku.models.tasks.enities.Task


sealed class GetAreaTasksResultState : ResultState {
    data class Success(val tasks: List<Task>) : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object Unauthorized : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object BadRequest: ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object Forbidden: ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object InternalServerError : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object Error : ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
    object None: ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState()
}


