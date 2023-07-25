package ru.kovsh.tasku.ui.area.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.ui.area.entities.NavigationStates
import ru.kovsh.tasku.ui.area.viewMode.AreaScreenViewModel
import ru.kovsh.tasku.ui.elements.AdditionalInfoEntities
import ru.kovsh.tasku.ui.elements.AreasPickDialog
import ru.kovsh.tasku.ui.elements.CalendarDialog
import ru.kovsh.tasku.ui.elements.GeneralScaffold
import ru.kovsh.tasku.ui.elements.PriorityPickDialog
import ru.kovsh.tasku.ui.elements.SortGroupRow
import ru.kovsh.tasku.ui.elements.TaskCollapsedButton
import ru.kovsh.tasku.ui.elements.TaskExpandedButton
import ru.kovsh.tasku.ui.elements.TaskuTopBar
import ru.kovsh.tasku.ui.shared.SharedViewModel
import ru.kovsh.tasku.ui.theme.Background
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
internal fun AreaScreenView(
    onUnauthorized: () -> Unit,
    onBackClicked: () -> Unit,
    onLoadAreas: () -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: AreaScreenViewModel,
    areaName: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (state.navigationState != NavigationStates.None) {
        viewModel.reset()
        if (state.navigationState == NavigationStates.Unauthorized)
            onUnauthorized()
        else
            onBackClicked()
    }
    AreaScreenView(
        onBackClicked = onBackClicked,
        onCreateTask = viewModel::onCreateTask,
        onEditTask = viewModel::onEditTask,
        onLoadAreas = onLoadAreas,
        areaName = areaName,
        newTaskText = state.newTaskText,
        areaTasks = viewModel.areaTasks,
        curAreas = sharedViewModel.curAreas,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF17171A)
@Composable
fun PrevAreaScreenView() {
    AreaScreenView(
        onBackClicked = {},
        areaName = "Area",
        newTaskText = ""
    )
}


@Composable
private fun AreaScreenView(
    onBackClicked: () -> Unit,
    onCreateTask: (Task) -> Unit = {},
    onEditTask: (Task) -> Unit = {},
    onLoadAreas: () -> Unit = {},
    modifier: Modifier = Modifier,
    areaName: String,
    newTaskText: String,
    areaTasks: List<Task> = listOf(),
    curAreas: List<Area> = listOf(),
) {
    val isNewTask = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        GeneralScaffold(
            focusRequester = focusRequester,
            onFabClicked = {
                isNewTask.value = true
            },
            topBar = {
                TaskuTopBar(
                    onBackClicked = onBackClicked,
                    screenTitle = areaName,
                    screenIcon = R.drawable.ic_main_menu_area
                )
            },
            content = {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Background),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SortGroupRow()
                    AreasList(
                        isNewTask = isNewTask.value,
                        focusRequester = focusRequester,
                        onCreateTask = {
                            onCreateTask(it.copy(name = areaName))
                            isNewTask.value = false
                        },
                        onEditTask = onEditTask,
                        onLoadAreas = onLoadAreas,
                        tasks = areaTasks,
                        curAreas = curAreas,
                    )
                }
            }
        )
    }
}

@Composable
fun AreasList(
    isNewTask: Boolean,
    onCreateTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit = {},
    onLoadAreas: () -> Unit = {},
    focusRequester: FocusRequester,
    tasks: List<Task> = listOf(),
    curAreas: List<Area> = listOf(),
) {
    val expandedTask = remember { mutableStateOf(-1) }
    val pickedAdditionalInfoButton =
        remember {
            mutableStateOf(
                Pair(
                    AdditionalInfoEntities.None,
                    Task(checked = false, inbox = false, someday = false, task_id = -1, title = "")
                )
            )
        }
    AdditionalInfoDialog(
        pickedAdditionalInfoButton = pickedAdditionalInfoButton.value,
        onAdditionalInfoChanged = { entity, task ->
            pickedAdditionalInfoButton.value = Pair(entity, task)
            onEditTask(task)
            pickedAdditionalInfoButton.value =
                Pair(AdditionalInfoEntities.None,
                    Task(checked = false, inbox = false, someday = false, task_id = -1, title = ""))
        },
        curAreas = curAreas,
        onLoadAreas = onLoadAreas,
    )
    val listState = rememberLazyListState()
    LaunchedEffect(isNewTask) {
        if (isNewTask) {
            expandedTask.value = -1
            listState.animateScrollToItem(0)
            focusRequester.requestFocus()
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = isNewTask,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TaskExpandedButton(
                    onAdditionalInfoButtonClicked = { entity, task ->
                        pickedAdditionalInfoButton.value = Pair(entity, task)
                    },
                    onCreateTask = onCreateTask,
                    listIndex = 0,
                    isNewTask = isNewTask,
                    focusRequester = focusRequester,
                )
            }
        }
        itemsIndexed(tasks) { index, task ->
            Box(
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(300)
                )
            ) {
                if (index != expandedTask.value) {
                    TaskCollapsedButton(
                        onTaskClicked = { expandedTask.value = index },
                        task = task,
                    )
                } else {
                    TaskExpandedButton(
                        listIndex = index,
                        onAdditionalInfoButtonClicked = { entity, task ->
                            pickedAdditionalInfoButton.value = Pair(entity, task)
                        },
                        task = task,
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionalInfoDialog(
    pickedAdditionalInfoButton: Pair<AdditionalInfoEntities, Task>,
    onAdditionalInfoChanged: (AdditionalInfoEntities, Task) -> Unit,
    onLoadAreas: () -> Unit = {},
    curAreas: List<Area> = listOf(),
) {
    if (pickedAdditionalInfoButton.first != AdditionalInfoEntities.None) {
        when (pickedAdditionalInfoButton.first) {
            AdditionalInfoEntities.Priority -> {
                PriorityPickDialog(
                    onDismissDialog = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.None,
                            pickedAdditionalInfoButton.second
                        )
                    },
                    onPriorityPicked = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.Priority,
                            pickedAdditionalInfoButton.second.copy(priority = it)
                        )
                    },
                    curPriority = pickedAdditionalInfoButton.second.priority ?: 0,
                )
            }
            AdditionalInfoEntities.Area -> {
                onLoadAreas()
                AreasPickDialog(
                    onDismissDialog = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.None,
                            pickedAdditionalInfoButton.second
                        )
                    },
                    onAreaPicked = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.Area,
                            pickedAdditionalInfoButton.second.copy(area_id = it)
                        )
                    },
                    currentAreaID = pickedAdditionalInfoButton.second.area_id ?: 0,
                    areas = curAreas,
                )
            }
            AdditionalInfoEntities.Date -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val initialDay = if (pickedAdditionalInfoButton.second.date.isNullOrEmpty()) {
                    LocalDate.now()
                } else {
                    LocalDate.parse(pickedAdditionalInfoButton.second.date, formatter)
                }
                CalendarDialog(
                    onDismissDialog = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.None,
                            pickedAdditionalInfoButton.second
                        )
                    },
                    onDayPicked = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.Date,
                            pickedAdditionalInfoButton.second.copy(date = it.atStartOfDay().format(formatter))
                        )
                    },
                    initialDay = initialDay,
                )
            }
            AdditionalInfoEntities.Deadline -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val initialDay = if (pickedAdditionalInfoButton.second.date.isNullOrEmpty()) {
                    LocalDate.now()
                } else {
                    LocalDate.parse(pickedAdditionalInfoButton.second.date, formatter)
                }
                CalendarDialog(
                    onDismissDialog = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.None,
                            pickedAdditionalInfoButton.second
                        )
                    },
                    onDayPicked = {
                        onAdditionalInfoChanged(
                            AdditionalInfoEntities.Deadline,
                            pickedAdditionalInfoButton.second.copy(deadline = it.atStartOfDay().format(formatter))
                        )
                    },
                    initialDay = initialDay,
                )
            }
            else -> {}
        }
    }
}