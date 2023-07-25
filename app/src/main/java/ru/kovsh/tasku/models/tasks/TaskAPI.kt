package ru.kovsh.tasku.models.tasks

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestBasic
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.models.tasks.enities.requestsBodies.AddTaskRequestBody
import ru.kovsh.tasku.models.tasks.enities.resultBodies.AddTaskResultBody

interface TaskAPI {
    @GET("task/area_tasks/{area_id}")
    suspend fun getAreaTasks(@Path("area_id") area_id: Long): List<Task>
    @GET("task/get_all")
    suspend fun getAllTasks(): List<Task>
    @POST("task/add_task")
    suspend fun addTask(@Body request: AddTaskRequestBody): AddTaskResultBody
    @POST("task/edit")
    suspend fun editTask(@Body task: Task)
}