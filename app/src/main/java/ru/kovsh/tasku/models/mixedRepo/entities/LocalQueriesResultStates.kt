package ru.kovsh.tasku.models.mixedRepo.entities

import ru.kovsh.tasku.models.tasks.enities.Task

sealed class LocalQueriesResultStates{
    data class Success(val tasks: List<Task>) : LocalQueriesResultStates()
    object Error : LocalQueriesResultStates()
    object None: LocalQueriesResultStates()
}