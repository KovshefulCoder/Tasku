package ru.kovsh.tasku.models.areas

import ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.GetAreasResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState

interface AreasRepository {
    suspend fun getAreas(): GetAreasResultState
    suspend fun addArea(title: String): AddAreaResultState
    suspend fun replaceAreas(
        topID: Long = -1,
        moveID: Long,
        bottomID: Long = -1,
    ): ReplaceAreasResultState
}
