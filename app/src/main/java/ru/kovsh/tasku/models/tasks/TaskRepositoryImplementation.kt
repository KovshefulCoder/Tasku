package ru.kovsh.tasku.models.tasks

import android.util.Log
import retrofit2.HttpException
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.models.tasks.enities.requestsBodies.AddTaskRequestBody
import ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetAllTasksResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.GetAreaTasksResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.UpdateTaskResultState
import java.net.UnknownHostException

class TaskRepositoryImplementation(private val api: TaskAPI):
    TaskRepository {
    override suspend fun getAreaTasks(areaId: Long): GetAreaTasksResultState {
        return try {
            val tasks = api.getAreaTasks(areaId)
            GetAreaTasksResultState.Success(tasks)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> GetAreaTasksResultState.BadRequest
                401 -> GetAreaTasksResultState.Unauthorized
                403 -> GetAreaTasksResultState.Forbidden
                500 -> GetAreaTasksResultState.InternalServerError
                else -> {
                    Log.e("getAreaTasks", "TaskRepositoryImplementation getAreas: unexpected httpException " + e.code().toString())
                    GetAreaTasksResultState.Error
                }
            }
        } catch (e: UnauthorizedException) {
            Log.e("getAreaTasks", "TaskRepositoryImplementation getAreaTasks: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: Exception) {
            Log.e("getAreaTasks", "Exception in Implementation of getAreaTasks:$e")
            GetAreaTasksResultState.Error
        }
    }

    override suspend fun getAllTasks(): GetAllTasksResultState {
        return try {
            val tasks = api.getAllTasks()
            Log.i("getAllTasks", "Success, ${tasks.size} tasks")
            GetAllTasksResultState.Success(tasks)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> {
                    Log.i("getAllTasks", "${e.code()}")
                    GetAllTasksResultState.Unauthorized
                }
                500 -> {
                    Log.i("getAllTasks", "${e.code()}")
                    GetAllTasksResultState.InternalServerError
                }
                else -> {
                    Log.e("getAllTasks",
                        "TaskRepositoryImplementation getAllTasks: unexpected httpException " + e.code()
                            .toString()
                    )
                    GetAllTasksResultState.Error
                }
            }
        }
        catch (e: KotlinNullPointerException) {
            Log.e("getAllTasks", "TaskRepositoryImplementation getAllTasks: KotlinNullPointerException :$e")
            GetAllTasksResultState.Success(emptyList())
        }
        catch (e: UnknownHostException) {
            Log.i("getAllTasks", "TaskRepositoryImplementation getAllTasks: UnknownHostException :$e")
            GetAllTasksResultState.NoServer
        }
        catch (e: Exception) {
            Log.e("getAllTasks", "Exception in Implementation of getAllTasks:$e")
            GetAllTasksResultState.Error
        }
    }

    override suspend fun addTask(task: Task): AddTaskResultState {
        return try {
            Log.i("addTask", "TaskRepositoryImplementation addTask: $task")
            val result = api.addTask(request = AddTaskRequestBody(task.area_id ?: 0L, task.priority ?: 0, task.title))
            AddTaskResultState.Success(result.task_id)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AddTaskResultState.BadRequest
                401 -> AddTaskResultState.Unauthorized
                403 -> AddTaskResultState.Forbidden
                500 -> AddTaskResultState.InternalServerError
                else -> {
                    Log.e("addTask", "TaskRepositoryImplementation addTask: unexpected httpException " + e.code().toString())
                    AddTaskResultState.Error
                }
            }
        } catch (e: UnauthorizedException) {
            Log.e("addTask", "TaskRepositoryImplementation addTask: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: Exception) {
            Log.e("addTask", "Exception in Implementation of addTask:$e")
            AddTaskResultState.Error
        }
    }

    override suspend fun editTask(task: Task): UpdateTaskResultState {
        return try {
            Log.i("editTask", "TaskRepositoryImplementation editTask: $task")
            api.editTask(task)
            UpdateTaskResultState.Success
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> UpdateTaskResultState.BadRequest
                401 -> UpdateTaskResultState.Unauthorized
                500 -> UpdateTaskResultState.InternalServerError
                else -> {
                    Log.e("editTask", "TaskRepositoryImplementation editTask: unexpected httpException " + e.code().toString())
                    UpdateTaskResultState.Error
                }
            }
        } catch (e: UnauthorizedException) {
            Log.e("editTask", "TaskRepositoryImplementation editTask: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: Exception) {
            Log.e("editTask", "Exception in Implementation of editTask:$e")
            UpdateTaskResultState.Error
        }
    }
}