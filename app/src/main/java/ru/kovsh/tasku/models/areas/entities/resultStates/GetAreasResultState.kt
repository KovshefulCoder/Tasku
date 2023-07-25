package ru.kovsh.tasku.models.areas.entities.resultStates

import ru.kovsh.tasku.models.areas.entities.Area

sealed class GetAreasResultState: ResultState {
    data class Success(val areas: List<Area>) : GetAreasResultState()
    object Unauthorized : GetAreasResultState()
    object InternalServerError : GetAreasResultState()
    object Error : GetAreasResultState()
    object NoServer: GetAreasResultState()
    object None: GetAreasResultState()
}

