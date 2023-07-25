package ru.kovsh.tasku.navigation.main_menu

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsh.tasku.ui.mainMenu.view.MainMenuScreen
import ru.kovsh.tasku.ui.mainMenu.viewModel.MainMenuViewModel
import ru.kovsh.tasku.ui.shared.SharedViewModel

fun NavGraphBuilder.menu(
    navController: NavController,
    onUnauthorized: () -> Unit,
    onSettingsClick: () -> Unit,
    onAreaClick: (Long, String) -> Unit,
    onMenuButtonClicked: (Int) -> Unit,
) {
    composable("Menu")
    { entry ->
        val sharedViewModel = entry.sharedViewModel<SharedViewModel>(navController)
        LaunchedEffect(Unit) {
            sharedViewModel.downloadAllTasks()
        }
        val menuViewModel: MainMenuViewModel = hiltViewModel()
        MainMenuScreen(
            onUnauthorized = {
                sharedViewModel.reset()
                menuViewModel.reset()
                onUnauthorized()
            },
            viewModel = menuViewModel,
            sharedViewModel = sharedViewModel,
            onAreaClick = onAreaClick,
            onSettingsClick = onSettingsClick,
            onRepeatLoadingTasks = { sharedViewModel.downloadAllTasks() },
            onResetRemoteState = { sharedViewModel.resetRemoteState() },
            onMenuButtonClicked = onMenuButtonClicked
        )
    }
}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}