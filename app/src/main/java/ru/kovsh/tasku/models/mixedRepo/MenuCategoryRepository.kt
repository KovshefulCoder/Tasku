package ru.kovsh.tasku.models.mixedRepo

import android.util.Log
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.mixedRepo.entities.LocalQueriesResultStates
import ru.kovsh.tasku.models.tasks.TaskRepository
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.UpdateTaskResultState
import javax.inject.Inject

class MenuCategoryRepository @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tasksDao: TasksDao
) {
    suspend fun getLocalInboxTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getInboxTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalInboxTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun getLocalAllTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getAllTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalAllTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun getLocalTodayTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getTodayTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalTodayTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun getLocalUpcomingTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getUpcomingTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalUpcomingTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun getLocalSomedayTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getSomedayTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalSomedayTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun getLocalFinishedTasks(): LocalQueriesResultStates {
        return try {
            val tasks = tasksDao.getFinishedTasks()
            LocalQueriesResultStates.Success(tasks)
        } catch (e: Exception) {
            Log.e("MenuCategoryRepository", "getLocalFinishedTasks: ${e.message}")
            LocalQueriesResultStates.Error
        }
    }

    suspend fun addTask(task: Task): LocalQueriesResultStates {
        var result: AddTaskResultState = AddTaskResultState.None
        try {
            result = taskRepository.addTask(task)
        } catch (e: UnauthorizedException) {
            Log.e("MenuCategoryRepository", "addRemoteTask: ${e.message}")
            throw e
        }
        return when (result) {
            is AddTaskResultState.Success -> {
                AddTaskResultState.Success(result.taskID)
                return try {
                    tasksDao.insert(task)
                    LocalQueriesResultStates.Success(listOf(task))
                } catch (e: Exception) {
                    Log.e("MenuCategoryRepository", "addLocalTask: ${e.message}")
                    LocalQueriesResultStates.Error
                }
            }
            else -> {
                LocalQueriesResultStates.Error
            }
        }
    }

    suspend fun updateTask(task: Task): LocalQueriesResultStates {
        var result: UpdateTaskResultState = UpdateTaskResultState.None
        try {
            result = taskRepository.editTask(task)
        } catch (e: UnauthorizedException) {
            Log.e("MenuCategoryRepository", "updateRemoteTask: ${e.message}")
            throw e
        }
        return when (result) {
            is UpdateTaskResultState.Success -> {
                try {
                    tasksDao.update(task)
                    LocalQueriesResultStates.Success(listOf(task))
                } catch (e: Exception) {
                    Log.e("MenuCategoryRepository", "updateLocalTask: ${e.message}")
                    LocalQueriesResultStates.Error
                }
            }
            else -> {
                LocalQueriesResultStates.Error
            }
        }
    }


}