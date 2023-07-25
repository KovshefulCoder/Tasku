package ru.kovsh.tasku.ui.mainMenuCategory.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsh.tasku.models.auth.UnauthorizedException
import ru.kovsh.tasku.models.mixedRepo.MenuCategoryRepository
import ru.kovsh.tasku.models.mixedRepo.entities.LocalQueriesResultStates
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.ui.area.entities.NavigationStates
import ru.kovsh.tasku.ui.mainMenuCategory.entities.CategoryStates
import ru.kovsh.tasku.ui.shared.entities.statesClasses.LoadingState
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val menuCategoryRepository: MenuCategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryStates())
    val state = _state.asStateFlow()

    private val _tasks: MutableList<Task> = mutableStateListOf()
    val tasks get() = _tasks

    fun reset() {
        viewModelScope.cancel()
        _state.update { CategoryStates() }
        _state.update {
            it.copy(loadingStatus = LoadingState.Closed)
        }
    }

    fun getLocalCategoryTasks(menuID: Int) {
        if (_state.value.loadingStatus != LoadingState.Closed) {
            viewModelScope.launch(Dispatchers.IO) {
                _state.update {
                    it.copy(loadingStatus = LoadingState.Loading)
                }
                val taskRequest = when (menuID) {
                    0 -> menuCategoryRepository.getLocalInboxTasks()
                    1 -> menuCategoryRepository.getLocalAllTasks()
                    2 -> menuCategoryRepository.getLocalTodayTasks()
                    3 -> menuCategoryRepository.getLocalUpcomingTasks()
                    4 -> menuCategoryRepository.getLocalSomedayTasks()
                    5 -> menuCategoryRepository.getLocalFinishedTasks()
                    else -> LocalQueriesResultStates.Error
                }
                when (taskRequest) {
                    is LocalQueriesResultStates.Success -> {
                        _state.update {
                            it.copy(loadingStatus = LoadingState.Success)
                        }
                        _tasks.clear()
                        _tasks.addAll(taskRequest.tasks)
                    }

                    is LocalQueriesResultStates.Error, LocalQueriesResultStates.None -> {
                        _state.update {
                            it.copy(loadingStatus = LoadingState.Error)
                        }
                        Log.i("CategoryViewModel", "getLocalCategoryTasks, error")
                    }
                }
            }
        }
    }

    fun onCreateTask(task: Task, menuID: Int) {
        viewModelScope.launch {
            val newTask = preprocessTaskWithMenuID(task, menuID)
            var result: LocalQueriesResultStates = LocalQueriesResultStates.None
            try {
                Log.i("onCreateTask", "Trying to add task $newTask")
                result = menuCategoryRepository.addTask(newTask)
            } catch (e: UnauthorizedException) {
                _state.update {
                    it.copy(navigationState = NavigationStates.Unauthorized)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(navigationState = NavigationStates.BackClicked)
                }
            }
            when (result) {
                is LocalQueriesResultStates.Success -> {
                    Log.i("onCreateTask", "Success, ${result.tasks}")
                    _tasks.add(newTask.copy(task_id = result.tasks[0].task_id))
                    //Yeah, wicked, cause LocalQueriesResultStates initially was created for get tasks queries
                }

                is LocalQueriesResultStates.Error, LocalQueriesResultStates.None -> {
                    _state.update {
                        it.copy(navigationState = NavigationStates.BackClicked)
                    }
                }
            }
        }
    }

    fun onUpdateTask(task: Task, menuID: Int) {
        viewModelScope.launch {
            val newTask = preprocessTaskWithMenuID(task, menuID)
            when (menuCategoryRepository.updateTask(newTask)) {
                is LocalQueriesResultStates.Success -> {
                    _tasks.indexOf(newTask).let {
                        _tasks[it] = newTask
                    }
                }

                is LocalQueriesResultStates.Error, LocalQueriesResultStates.None -> {
                    _state.update {
                        it.copy(navigationState = NavigationStates.BackClicked)
                    }
                }
            }
        }
    }
}

fun preprocessTaskWithMenuID(task: Task, menuID: Int): Task {
    return when (menuID) {
        0 -> task.copy(inbox = true)
        4 -> task.copy(someday = true)
        else -> task
    }
}