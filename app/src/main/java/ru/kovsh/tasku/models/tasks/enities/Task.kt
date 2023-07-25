package ru.kovsh.tasku.models.tasks.enities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    val area_id: Long? = 0L, //Optional
    val checked: Boolean?,
    val date: String? = "",  //Optional
    val deadline: String? = "",  //Optional
    val description: String? = "",  //Optional
    val name: String? = "", //Area name  //Optional
    val priority: Int? = 0,  //Optional
    val tag: String? = "",  //Optional
    @PrimaryKey
    val task_id: Long?,
    val title: String,
    val inbox: Boolean?,
    val someday: Boolean?,
)