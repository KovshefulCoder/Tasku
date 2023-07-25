package ru.kovsh.tasku.models.mixedRepo

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.withContext
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.auth.AuthRepository
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.local.AreaDao
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.tasks.TaskRepository
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetRemoteTasksResultState
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class SharedRepository @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tasksDao: TasksDao,
    private val areaDao: AreaDao
) {

    suspend fun getRemoteTasks(): GetRemoteTasksResultState {
        val result: GetAllTasksResultState
        Log.i("downloadAllTasks", "Start")
        try {
            result = taskRepository.getAllTasks()
        } catch (e: UnauthorizedException) {
            return GetRemoteTasksResultState.Unauthorized
        }
        return when (result) {
            is GetAllTasksResultState.Success -> {
                Log.i("downloadAllTasks", "Success, ${result.tasks.size} tasks")
                withContext(coroutineContext) {
                    tasksDao.insertTasks(result.tasks)
                }
                Log.i("downloadAllTasks", "Success, ${result.tasks.size} tasks inserted")
                GetRemoteTasksResultState.Success
            }

            is GetAllTasksResultState.Unauthorized -> GetRemoteTasksResultState.Unauthorized
            is GetAllTasksResultState.NoServer -> GetRemoteTasksResultState.NoServer
            else -> GetRemoteTasksResultState.Error
        }
    }

    suspend fun getLocalAreas(): List<Area> {
        return areaDao.getAllAreas()
    }
}