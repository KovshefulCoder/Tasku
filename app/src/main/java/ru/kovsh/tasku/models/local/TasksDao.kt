package ru.kovsh.tasku.models.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ru.kovsh.tasku.models.tasks.enities.Task

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks WHERE area_id = :area_id")
    suspend fun getAreaTasks(area_id: Long) : List<Task>

    @Upsert
    suspend fun insertTasks(tasks: List<Task>)

    @Upsert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM tasks WHERE inbox = 1")
    suspend fun getInboxTasks() : List<Task>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks() : List<Task>

    @Query("SELECT * FROM tasks WHERE date(date) = date('now')")
    suspend fun getTodayTasks() : List<Task>

    @Query("SELECT * FROM tasks WHERE date(date) > date('now')")
    suspend fun getUpcomingTasks() : List<Task>

    @Query("SELECT * FROM tasks WHERE someday = 1")
    suspend fun getSomedayTasks() : List<Task>

    @Query("SELECT * FROM tasks WHERE checked = 1")
    suspend fun getFinishedTasks() : List<Task>

}