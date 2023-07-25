package ru.kovsh.tasku.ui.mainMenuCategory.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.ui.area.entities.NavigationStates
import ru.kovsh.tasku.ui.area.view.AreasList
import ru.kovsh.tasku.ui.elements.GeneralScaffold
import ru.kovsh.tasku.ui.elements.SortGroupRow
import ru.kovsh.tasku.ui.elements.TaskuTopBar
import ru.kovsh.tasku.ui.mainMenu.entities.MenuCategory
import ru.kovsh.tasku.ui.mainMenu.entities.menuCategories
import ru.kovsh.tasku.ui.mainMenuCategory.viewModel.CategoryViewModel
import ru.kovsh.tasku.ui.shared.SharedViewModel


@Composable
internal fun MenuCategoryScreen(
    onUnauthorized: () -> Unit,
    onBackClicked: () -> Unit,
    viewModel: CategoryViewModel,
    sharedViewModel: SharedViewModel,
    menuID: Int,
) {
    LaunchedEffect(Unit) {
        viewModel.getLocalCategoryTasks(menuID)
    }
    val sharedState = sharedViewModel.state.collectAsStateWithLifecycle()
    val categoryState by viewModel.state.collectAsStateWithLifecycle()

    if (categoryState.navigationState != NavigationStates.None) {
        viewModel.reset()
        if (categoryState.navigationState == NavigationStates.Unauthorized)
            onUnauthorized()
        else
            onBackClicked()
    }

    MenuCategoryScreen(
        onUnauthorized = onUnauthorized,
        onBackClicked = {
            viewModel.reset()
            onBackClicked()
        },
        onCreateTask = {
            viewModel.onCreateTask(it, menuID)
        },
        onUpdateTask = {
            viewModel.onUpdateTask(it, menuID)
        },
        menuName = menuCategories[menuID].nameID,
        iconID = menuCategories[menuID].iconID,
        categoryTasks = viewModel.tasks,
    )
}

@Preview
@Composable
private fun MenuCategoryScreen(
    onUnauthorized: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onCreateTask: (Task) -> Unit = {},
    onUpdateTask: (Task) -> Unit = {},
    modifier: Modifier = Modifier,
    menuName: Int = 0,
    iconID: Int = 0,
    categoryTasks: List<Task> = listOf(),
) {
    val isNewTask = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        GeneralScaffold(
            onFabClicked = {
                isNewTask.value = true
            },
            focusRequester = focusRequester,
            topBar = {
                TaskuTopBar(
                    onBackClicked = onBackClicked,
                    screenTitle = stringResource(id = menuName),
                    screenIcon = iconID
                )
            },
            content = {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SortGroupRow()
                    AreasList(
                        isNewTask = isNewTask.value,
                        focusRequester = focusRequester,
                        onCreateTask = {
                            onCreateTask(it)
                            isNewTask.value = false
                        },
                        onLoadAreas = {},
                        tasks = categoryTasks,
                    )
                }
            }
        )
    }
}