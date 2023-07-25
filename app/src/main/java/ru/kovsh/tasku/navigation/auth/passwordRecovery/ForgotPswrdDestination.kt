package ru.kovsh.tasku.navigation.auth.passwordRecovery

import android.net.Uri
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery.ForgotPswrdViewModel
import ru.kovsh.tasku.ui.auth.views.passwordRecovery.ForgotPasswordView

private const val EMAIL = "email"

fun NavGraphBuilder.forgotPassword(
){
    composable(
        route = "forgotPassword/?email={$EMAIL}",
        arguments = listOf(
            navArgument(EMAIL) {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        }),
    ) {NavBackStackEntry ->
        val forgotPswrdViewModel: ForgotPswrdViewModel = hiltViewModel()
        val encodedEmail = NavBackStackEntry.arguments?.getString(EMAIL)
        val email = Uri.decode(encodedEmail)
        if (email?.isNotEmpty() == true) {
            forgotPswrdViewModel.onEmailChanged(email)
        }
        ForgotPasswordView(viewModel = forgotPswrdViewModel)
    }
}

fun NavController.navigateToForgotPassword(
    email: String
) {
    val route = "forgotPassword/?email=$email"
    navigate(route, navOptions = navOptions {
            launchSingleTop = true
        }
    )
}