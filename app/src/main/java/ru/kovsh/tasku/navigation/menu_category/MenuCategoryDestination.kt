package ru.kovsh.tasku.navigation.menu_category

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.kovsh.tasku.navigation.main_menu.sharedViewModel
import ru.kovsh.tasku.ui.mainMenuCategory.view.MenuCategoryScreen
import ru.kovsh.tasku.ui.mainMenuCategory.viewModel.CategoryViewModel
import ru.kovsh.tasku.ui.shared.SharedViewModel

const val MENU_ID = "menuID"

fun NavGraphBuilder.menuCategory(
    onBackClicked: () -> Unit,
    onUnauthorized: () -> Unit,
    navController: NavController,
) {
    composable(
        route = "MenuCategory/{$MENU_ID}",
        arguments = listOf(
            navArgument(MENU_ID) {
                type = NavType.IntType
                defaultValue = 0
            },
        ),
    ) { entry ->
        val menuID = entry.arguments?.getInt(MENU_ID)
        val sharedViewModel = entry.sharedViewModel<SharedViewModel>(navController)
        val categoryViewModel: CategoryViewModel = hiltViewModel()
        if (menuID == null) {
            onUnauthorized()
        } else {
            MenuCategoryScreen(
                onUnauthorized = onUnauthorized,
                onBackClicked = onBackClicked,
                viewModel = categoryViewModel,
                sharedViewModel = sharedViewModel,
                menuID = menuID,
            )
        }
    }
}


fun NavController.navigateToMenuCategory(
    menuID: Int,
) {
    val route = "MenuCategory/$menuID"
    navigate(route)
}
