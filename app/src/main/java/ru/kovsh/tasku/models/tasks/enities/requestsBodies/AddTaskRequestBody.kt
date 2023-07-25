package ru.kovsh.tasku.models.tasks.enities.requestsBodies

data class AddTaskRequestBody(
    val area_id: Long,
    val priority: Int,
    val title: String
)