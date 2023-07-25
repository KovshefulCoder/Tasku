package ru.kovsh.tasku.ui.area.viewMode

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.local.TasksDao
import ru.kovsh.tasku.models.mixedRepo.MenuCategoryRepository
import ru.kovsh.tasku.models.tasks.TaskRepository
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.models.tasks.enities.resultStates.AddTaskResultState
import ru.kovsh.tasku.models.tasks.enities.resultStates.UpdateTaskResultState
import ru.kovsh.tasku.ui.area.entities.AreaScreenStates
import ru.kovsh.tasku.ui.area.entities.NavigationStates
import javax.inject.Inject

@HiltViewModel
class AreaScreenViewModel @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val tasksDao: TasksDao,
) : ViewModel() {

    private val _state = MutableStateFlow(AreaScreenStates())
    val state = _state.asStateFlow()
    private val _areaTasks: MutableList<Task> = mutableStateListOf()
    val areaTasks get() = _areaTasks
    private val _areaID = MutableStateFlow(0L)

    fun reset() {
        viewModelScope.cancel()
        _state.update { AreaScreenStates() }
        _areaTasks.clear()
    }

    fun getAreaTasks(areaID: Long) {
        viewModelScope.launch {
            _areaID.update { areaID }
            _areaTasks.clear()
            _areaTasks.addAll(tasksDao.getAreaTasks(areaID))
        }
    }

    fun onCreateTask(task: Task) {
        viewModelScope.launch {
            var result: AddTaskResultState = AddTaskResultState.None
            try {
                Log.i("onCreateTask", "Trying to add task $task")
                result = tasksRepository.addTask(task.copy(area_id = _areaID.value))
            } catch (e: UnauthorizedException) {
                _state.update {
                    it.copy(navigationState = NavigationStates.Unauthorized)
                }
            }
            catch (e: Exception) {
                _state.update {
                    it.copy(navigationState = NavigationStates.BackClicked)
                }
            }
            when(result) {
                is AddTaskResultState.Success -> {
                    Log.i("onCreateTask", "Success, ${result.taskID}")
                    try {
                        tasksDao.insert(task.copy(area_id = _areaID.value, task_id = result.taskID))
                        _areaTasks.clear()
                        _areaTasks.addAll(tasksDao.getAreaTasks(_areaID.value))
                        _areaTasks.sortBy { it.title }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(navigationState = NavigationStates.BackClicked)
                        }
                    }
                }
                else -> {
                    _state.update {
                        it.copy(navigationState = NavigationStates.BackClicked)
                    }
                }
            }
        }
    }

    fun onEditTask(task: Task) {
        viewModelScope.launch {
            var result: UpdateTaskResultState = UpdateTaskResultState.None
            try {
                Log.i("onUpdateTask", "Trying to update task $task")
                result = tasksRepository.editTask(task)
            } catch (e: UnauthorizedException) {
                _state.update {
                    it.copy(navigationState = NavigationStates.Unauthorized)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(navigationState = NavigationStates.BackClicked)
                }
            }
            when(result) {
                is UpdateTaskResultState.Success -> {
                    Log.i("onUpdateTask", "Success")
                    try {
                        tasksDao.update(task)
                        _areaTasks.clear()
                        _areaTasks.addAll(tasksDao.getAreaTasks(_areaID.value))
                        _areaTasks.sortBy { it.title }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(navigationState = NavigationStates.BackClicked)
                        }
                    }
                }
                else -> {
                    _state.update {
                        it.copy(navigationState = NavigationStates.BackClicked)
                    }
                }
            }
        }
    }
}