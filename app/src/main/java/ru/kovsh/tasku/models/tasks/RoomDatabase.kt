package ru.kovsh.tasku.models.tasks

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.local.AreaDao
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.tasks.enities.Task

@Database(entities = [Task::class, Area::class], version = 7)
abstract class TaskuRoomDatabase: RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun areasDao(): AreaDao
}