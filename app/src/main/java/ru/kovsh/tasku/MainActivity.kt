package ru.kovsh.tasku

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.kovsh.tasku.navigation.area.area
import ru.kovsh.tasku.navigation.area.navigateToArea
import ru.kovsh.tasku.navigation.auth.auth
import ru.kovsh.tasku.navigation.auth.passwordRecovery.forgotPassword
import ru.kovsh.tasku.navigation.auth.passwordRecovery.navigateToForgotPassword
import ru.kovsh.tasku.navigation.auth.passwordRecovery.newPassword
import ru.kovsh.tasku.navigation.main_menu.menu
import ru.kovsh.tasku.navigation.main_menu.settings
import ru.kovsh.tasku.navigation.menu_category.menuCategory
import ru.kovsh.tasku.navigation.menu_category.navigateToMenuCategory
import ru.kovsh.tasku.ui.auth.viewModels.AuthScreenViewModel
import ru.kovsh.tasku.ui.theme.Background
import ru.kovsh.tasku.ui.theme.TaskuTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Do not touch navController, it`s here because of handleIntent, onNewIntent
            navController = rememberNavController()
            TaskuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background,
                ) {
                    TaskuApp(
                        navController = navController
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("lifecycleA", "Activity onNewIntent: $this")
        handleIntent(intent)
        if (intent != null) {
            intent.data = null
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null || intent.data == null) {
            return
        }
        if (intent.dataString?.contains("temp") == true) {
            navController.navigate(
                "Auth?confirmToken=${intent.dataString?.substringAfterLast("/")}",
            )
        } else if (intent.dataString?.contains("temp") == true) {
            navController.navigate(
                "Auth?newPassword=${intent.dataString?.substringAfterLast("/")}",
            )
        }
    }
}


@Composable
fun TaskuApp(
    navController: NavController
) {
    //absolutely wrong, fixed 2-days problem with this, need to be created inside Authentication sub nav graph
    val viewModel: AuthScreenViewModel = hiltViewModel()
    NavHost(
        navController = navController as NavHostController,
        startDestination = "MainApp"
    ) {
        navigation(
            route = "Authentication",
            startDestination = "Auth"
        ) {
            auth(
                onSuccessfulAuth = {
                    navController.navigate("MainApp", navOptions {
                        launchSingleTop = true
                        popUpTo("Authentication") {
                            inclusive = true
                        }
                    })
                },
                onForgotPassword = { navController.navigateToForgotPassword(it) },
                viewModel = viewModel
            )
            forgotPassword()
            newPassword(
                onSuccessfulAuth = {
                    navController.navigate("MainApp", navOptions {
                        launchSingleTop = true
                        popUpTo("Authentication") {
                            inclusive = true
                        }
                    })
                },
            )
        }
        navigation(
            route = "MainApp",
            startDestination = "Menu"
        ) {
            fun onUnauthorized() {
                navController.navigate("Authentication") {
                    popUpTo("MainApp") {
                        inclusive = true
                        saveState = true
                    }
                }
            }
            menu(
                onUnauthorized = { onUnauthorized() },
                onAreaClick = { id, name -> navController.navigateToArea(id, name) },
                onSettingsClick = { navController.navigate("Settings") },
                onMenuButtonClicked = {
                    navController.navigateToMenuCategory(it)
                },
                navController = navController,
            )
            area(
                onUnauthorized = { onUnauthorized() },
                onBackClicked = { navController.popBackStack() },
                navController = navController,
            )
            settings(
                onUnauthorized = { onUnauthorized() },
                onBackClicked = { navController.popBackStack() }
            )
            menuCategory(
                onUnauthorized = { onUnauthorized() },
                onBackClicked = { navController.popBackStack() },
                navController = navController
            )
        }
    }
}