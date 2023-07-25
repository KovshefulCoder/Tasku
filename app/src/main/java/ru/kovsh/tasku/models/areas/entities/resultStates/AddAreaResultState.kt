package ru.kovsh.tasku.models.areas.entities.resultStates

import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.areas.entities.resultBodies.AddAreaResultBody

sealed class AddAreaResultState: ru.kovsh.tasku.models.areas.entities.resultStates.ResultState {
    data class Success(val area_id: Long) : ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object BadRequest: ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object Forbidden: ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object Unauthorized : ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object InternalServerError : ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object Error : ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
    object None: ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState()
}
