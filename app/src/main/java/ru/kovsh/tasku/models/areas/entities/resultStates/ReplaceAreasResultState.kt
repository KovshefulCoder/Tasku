package ru.kovsh.tasku.models.areas.entities.resultStates

sealed class ReplaceAreasResultState:
    ru.kovsh.tasku.models.areas.entities.resultStates.ResultState {
    object Success : ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
    object Unauthorized : ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
    object Forbidden: ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
    object InternalServerError : ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
    object Error : ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
    object None: ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState()
}