package ru.kovsh.tasku.navigation.main_menu

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.ui.mainMenu.view.SettingsScreen
import ru.kovsh.tasku.ui.mainMenu.viewModel.SettingsViewModel

fun NavGraphBuilder.settings(
    onBackClicked: () -> Unit,
    onUnauthorized: () -> Unit,
) {
   composable("Settings") {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(
            viewModel = settingsViewModel,
            onBackClicked = onBackClicked,
            onUnauthorized = onUnauthorized
        )
    }
}