package ru.kovsh.tasku.models.tasks

import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.UpdateTaskResultState

interface TaskRepository {
    suspend fun getAreaTasks(areaId: Long): GetAreaTasksResultState
    suspend fun getAllTasks(): GetAllTasksResultState
    suspend fun addTask(task: Task): AddTaskResultState
    suspend fun editTask(task: Task): UpdateTaskResultState
}