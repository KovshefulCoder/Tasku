package ru.kovsh.tasku.navigation.area

import android.net.Uri
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.kovsh.tasku.navigation.main_menu.sharedViewModel
import ru.kovsh.tasku.ui.area.view.AreaScreenView
import ru.kovsh.tasku.ui.area.viewMode.AreaScreenViewModel
import ru.kovsh.tasku.ui.shared.SharedViewModel

private const val AREA_NAME = "areaName"
private const val AREA_ID = "areaID"


fun NavGraphBuilder.area(
    onBackClicked: () -> Unit,
    onUnauthorized: () -> Unit,
    navController: NavController,
) {
    composable(
        route = "area/{$AREA_ID}/{$AREA_NAME}",
        arguments = listOf(
            navArgument(AREA_ID) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(AREA_NAME) {
                type = NavType.StringType
                defaultValue = ""
            }
        ),
    )
    { entry ->
        val encodedAreaName = entry.arguments?.getString(AREA_NAME)
        val areaID = entry.arguments?.getLong(AREA_ID)
        if (encodedAreaName == null || areaID == null) {
            onUnauthorized()
        } else {
            val sharedViewModel = entry.sharedViewModel<SharedViewModel>(navController)
            val areaScreenViewModel: AreaScreenViewModel = hiltViewModel()
            val areaName = Uri.decode(encodedAreaName)
            areaScreenViewModel.getAreaTasks(areaID)
            AreaScreenView(
                onUnauthorized = onUnauthorized,
                onBackClicked = onBackClicked,
                viewModel = areaScreenViewModel,
                sharedViewModel = sharedViewModel,
                onLoadAreas = sharedViewModel::getLocalAreas,
                areaName = areaName
            )
        }
    }
}

fun NavController.navigateToArea(
    areaID: Long,
    areaName: String
) {
    val encodedAreaName = Uri.encode(areaName)
    val route = "area/$areaID/$encodedAreaName"
    Log.i("Navigation", "Navigating to $route")
    navigate(route)
}
