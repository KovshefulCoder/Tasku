package ru.kovsh.tasku.navigation.auth

import android.util.Log
import androidx.navigation.*
import androidx.navigation.compose.composable
import ru.kovsh.tasku.ui.auth.viewModels.AuthScreenViewModel
import ru.kovsh.tasku.ui.auth.views.AuthenticationScreen

private const val CONFIRM_TKN = "confirmToken"

fun NavGraphBuilder.auth(
    onSuccessfulAuth: () -> Unit,
    onForgotPassword: (String) -> Unit,
    viewModel: AuthScreenViewModel
) {

    composable(
        route = "Auth?confirmToken={$CONFIRM_TKN}", //TODO(Changed without checking)
        arguments = listOf(navArgument(CONFIRM_TKN) {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        }),
    ) {NavBackStackEntry ->
        val confirmToken = NavBackStackEntry.arguments?.getString(CONFIRM_TKN)
        if (!confirmToken.isNullOrEmpty()) {
            Log.i("auth", "Going to authenticate with token: $confirmToken")
            viewModel.onAuthenticate(confirmToken)
        }
        AuthenticationScreen(
            viewModel = viewModel,
            onSuccessfulAuth = onSuccessfulAuth,
            onForgotPassword = onForgotPassword
        )
    }
}