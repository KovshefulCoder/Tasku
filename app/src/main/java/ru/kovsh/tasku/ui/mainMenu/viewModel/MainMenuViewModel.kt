package ru.kovsh.tasku.ui.mainMenu.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.areas.AreasRepository
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.GetAreasResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.local.AreaDao
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState
import ru.kovsh.tasku.ui.elements.AdditionalInfoEntities
import ru.kovsh.tasku.ui.mainMenu.entities.AreaUI
import ru.kovsh.tasku.ui.mainMenu.entities.FabStates
import ru.kovsh.tasku.ui.mainMenu.entities.MainMenuStates
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val areasRepository: AreasRepository,
    private val areaDao: AreaDao
) : ViewModel() {
    init {
        loadAreas()
    }

    private val _screenState = MutableStateFlow(MainMenuStates())
    val screenState = _screenState.asStateFlow()
    private val _areas: MutableList<AreaUI> = mutableStateListOf()
    val areas get() = _areas

    private val _newArea = MutableStateFlow(AreaUI())
    private val _fabState = MutableStateFlow(FabStates())
    val fabState = _fabState.asStateFlow()

    fun reset() {
        viewModelScope.cancel()
        _screenState.update { MainMenuStates() }
        _newArea.update { AreaUI() }
        _fabState.update { FabStates() }
    }

    private fun loadAreas() {
        viewModelScope.launch {
            val result: GetAreasResultState
            try {
                result = areasRepository.getAreas()
            } catch (e: UnauthorizedException) {
                _screenState.update {
                    it.copy(isAuthorized = UserAuthenticationState.Unauthorized)
                }
                return@launch
            }
            when (result) {
                is GetAreasResultState.Success -> {
                    areaDao.insertAreas(result.areas)
                    _areas.clear()
                    for (area in result.areas) {
                        _areas.add(AreaUI(id = area.area_id, name = area.title))
                    }
                    _screenState.update {
                        it.copy(isAuthorized = UserAuthenticationState.Authorized)
                    }
                }
                is GetAreasResultState.NoServer -> {
                    _screenState.update {
                        it.copy(isAuthorized = UserAuthenticationState.NoServer)
                    }
                }
                else -> {
                    _screenState.update {
                        it.copy(isAuthorized = UserAuthenticationState.Unauthorized)
                    }
                }
            }
        }
    }

    fun onAddNewArea(title: String) {
        viewModelScope.launch {
            val result: AddAreaResultState
            try {
                result = areasRepository.addArea(title)
            } catch (e: UnauthorizedException) {
                _screenState.update {
                    it.copy(isAuthorized = UserAuthenticationState.Unauthorized)
                }
                return@launch
            }
            when (result) {
                is AddAreaResultState.Success -> {
                    _newArea.update {
                        it.copy(id = result.area_id, name = title)
                    }
                }

                else -> {
                    _newArea.update { AreaUI() }
                    return@launch
                }
            }
            //And then POST to resort them
            val lastAreaIndex = _areas.size - 1
            val newAreaIndex = _screenState.value.newElementIndex
            if (newAreaIndex < lastAreaIndex) {
                val resultReplaceArea: ReplaceAreasResultState
                try {
                    when (newAreaIndex) {
                        -1 -> {
                            resultReplaceArea = areasRepository.replaceAreas(
                                moveID = _newArea.value.id,
                                bottomID = _areas[0].id,
                            )
                        }

                        0 -> {
                            resultReplaceArea = areasRepository.replaceAreas(
                                topID = _areas[0].id,
                                moveID = _newArea.value.id,
                                bottomID = _areas[1].id
                            )
                        }

                        -2 -> {
                            _newArea.update { AreaUI() }
                            _screenState.update { it.copy(newElementIndex = -2) }
                            return@launch
                        }

                        else -> {
                            resultReplaceArea = areasRepository.replaceAreas(
                                topID = _areas[newAreaIndex].id,
                                moveID = _newArea.value.id,
                                bottomID = _areas[newAreaIndex + 1].id
                            )
                        }
                    }
                } catch (e: UnauthorizedException) {
                    _screenState.update {
                        it.copy(isAuthorized = UserAuthenticationState.Unauthorized)
                    }
                    return@launch
                }
                when (resultReplaceArea) {
                    is ReplaceAreasResultState.Success -> {
                        if (newAreaIndex == -1 || _areas.isEmpty()) {
                            areaDao.insert(
                                Area(
                                area_id = _newArea.value.id,
                                title = _newArea.value.name)
                            )
                            _areas.add(0, _newArea.value)
                        } else {
                            areaDao.insert(
                                Area(
                                    area_id = _newArea.value.id,
                                    title = _newArea.value.name)
                            )
                            _areas.add(newAreaIndex + 1, _newArea.value)
                        }
                        _screenState.update {
                            it.copy(newElementIndex = -2)
                        }
                    }

                    else -> {
                        Log.e("add areas", "error: $resultReplaceArea")
                        _newArea.update { AreaUI() }
                        _screenState.update { it.copy(newElementIndex = -2) }
                        return@launch
                    }
                }
            } else {
                areaDao.insert(
                    Area(
                        area_id = _newArea.value.id,
                        title = _newArea.value.name)
                )
                _areas.add(_newArea.value)
            }
            _newArea.update { AreaUI() }
            _screenState.update { it.copy(newElementIndex = -2) }
            //loadAreas()
        }
    }

    fun onCalculateLazySizesForFAB(elementWidth: Int, elementHeight: Int, height: Dp) {
        viewModelScope.launch {
            _fabState.update {
                it.copy(
                    elementsSize = Pair(elementWidth, elementHeight),
                    textFieldUnderFabHeight = height
                )
            }
        }
    }


    fun onFabChange(x: Float, y: Float) {
        viewModelScope.launch {
            if (x == 0f && y == 0f) {
                _fabState.update { value -> value.copy(textFieldUnderFabIndex = -2) }
                return@launch
            }
            val areasCoordinates = _screenState.value.visibleAreasIndexes
            areasCoordinates.forEach { (key, value) ->
                if (x >= value.first && x <= value.first + _fabState.value.elementsSize.first &&
                    y >= value.second && y <= value.second + _fabState.value.elementsSize.second
                ) {
                    _fabState.update { it.copy(textFieldUnderFabIndex = key) }
                    return@launch
                }
            }
            // min by id -> first in current visible lizy list ->
            // the same coordinates have Lazy list itself
            if (areasCoordinates.isEmpty() && _areas.isEmpty()) {
                _fabState.update { it.copy(textFieldUnderFabIndex = -1) }
                return@launch
            } else {
                val minKey = areasCoordinates.keys.min()
                val lazyCoordinates = areasCoordinates[minKey] ?: Pair(0f, 0f)
                if (y < lazyCoordinates.second && x >= lazyCoordinates.first) {
                    _fabState.update { it.copy(textFieldUnderFabIndex = minKey - 1) }
                    return@launch
                }
                val maxKey = areasCoordinates.keys.max()
                val lastAreaCoordinates = areasCoordinates[maxKey] ?: Pair(0f, 0f)
                if (y > lastAreaCoordinates.second && x >= lastAreaCoordinates.first) {
                    _fabState.update { it.copy(textFieldUnderFabIndex = maxKey) }
                    return@launch
                }
            }
        }
    }

    fun onUpdateVisibleAreas(newVisibleAreas: MutableMap<Int, Pair<Float, Float>>) {
        viewModelScope.launch {
            _screenState.update {
                it.copy(
                    visibleAreasIndexes = newVisibleAreas
                )
            }
        }
    }

    fun onFabClick() {
        Log.i(
            "refactor visibility",
            "visibleAreasIndexes = ${_screenState.value.visibleAreasIndexes}"
        )
    }

    fun onFabStart() {
        _screenState.update {
            it.copy(
                newElementIndex = -2
            )
        }
    }

    fun onFabEnd() {
        _screenState.update {
            it.copy(
                newElementIndex = _fabState.value.textFieldUnderFabIndex
            )
        }
        _fabState.update {
            it.copy(
                textFieldUnderFabIndex = -2
            )
        }
    }
}