package ru.kovsh.tasku.ui.auth.views.passwordRecovery

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthDialog
import ru.kovsh.tasku.ui.auth.views.StatusDialog

@Composable
fun StatusDialogForgotPswrd(
    onDialogBackClicked: () -> Unit,
    authRequestState: AuthRequestsStates,
    email: String,
) {
    when(authRequestState) {
        is AuthRequestsStates.Success -> {
            StatusDialog(
                onDialogBackClicked,
                AuthDialog(
                    iconID = R.drawable.inbox_icon,
                    title = stringResource(id = R.string.success_forgot_pswrd_text) + " $email",
                )
            )
        }
        else -> {
        }
    }
    
}