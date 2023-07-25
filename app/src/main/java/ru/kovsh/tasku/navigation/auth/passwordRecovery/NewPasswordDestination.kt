package ru.kovsh.tasku.navigation.auth.passwordRecovery

import android.net.Uri
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery.ForgotPswrdViewModel
import ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery.NewPswrdViewModel
import ru.kovsh.tasku.ui.auth.views.passwordRecovery.ForgotPasswordView
import ru.kovsh.tasku.ui.auth.views.passwordRecovery.NewPasswordView

private const val RESTORE_TOKEN = "restoreRefresh"

fun NavGraphBuilder.newPassword(
    onSuccessfulAuth: () -> Unit
){
    composable(
        route = "Auth?newPassword={$RESTORE_TOKEN}",
        arguments = listOf(navArgument(RESTORE_TOKEN) {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        }),
        deepLinks = listOf(navDeepLink {
            uriPattern = "https://m1.itsk.pw/auth/restore_password/{$RESTORE_TOKEN}"
        })
    ) {NavBackStackEntry ->
        val newPswrdViewModel: NewPswrdViewModel = hiltViewModel()
        val restoreToken = NavBackStackEntry.arguments?.getString(RESTORE_TOKEN)
        if (restoreToken?.isNotEmpty() == true) {
            newPswrdViewModel.onSaveToken(restoreToken)
        }
        NewPasswordView(
            onSuccessfulAuth = onSuccessfulAuth,
            viewModel = newPswrdViewModel
        )
    }
}