package ru.kovsh.tasku.ui.auth.views.passwordRecovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates
import ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery.ForgotPswrdViewModel
import ru.kovsh.tasku.ui.auth.views.EmailTextField
import ru.kovsh.tasku.ui.auth.views.Logo
import ru.kovsh.tasku.ui.auth.views.MainConfirmButton
import ru.kovsh.tasku.ui.theme.Background
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.typography

@Composable
internal fun ForgotPasswordView(
    viewModel: ForgotPswrdViewModel,
) {
    val state by viewModel.state.collectAsState()
    ForgotPasswordView(
        onEmailChanged = viewModel::onEmailChanged,
        onButtonClicked = viewModel::onSendButtonClicked,
        email = state.email,
        isValidEmail = state.isValidEmail,
        authRequestsState = state.authResult,
        onDialogBackClicked = viewModel::onDialogBackClicked
    )
}

@Preview(backgroundColor = 0xFF17171A)
@Composable
fun PrevForgotPasswordView() {
    ForgotPasswordView(
        onEmailChanged = {},
        onButtonClicked = {},
        email = "",
        isValidEmail = ValidityStates.Valid,
        authRequestsState = AuthRequestsStates.None,
        onDialogBackClicked = {}
    )
}


@Composable
private fun ForgotPasswordView(
    onEmailChanged: (String) -> Unit,
    onButtonClicked: () -> Unit,
    onDialogBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    email: String,
    isValidEmail: ValidityStates,
    authRequestsState: AuthRequestsStates,


) {
    StatusDialogForgotPswrd(
        onDialogBackClicked = onDialogBackClicked,
        authRequestState = authRequestsState, email = email)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Logo(modifier = Modifier.weight(3f, false ))
            Text(
                modifier = Modifier.weight(1f, false),
                text = stringResource(id = R.string.password_recovery_title),
                textAlign = TextAlign.Center,
                color = Base0,
                style = typography.h2,
            )
            Text(
                modifier = Modifier.weight(1f, false),
                text = stringResource(id = R.string.forgot_password_text),
                textAlign = TextAlign.Center,
                color = Base500,
                style = typography.h4.copy(
                    fontWeight = FontWeight.Normal
                ),
            )
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween)
            {
                EmailTextField(
                    modifier = Modifier.weight(1f,),
                    onEmailChanged = onEmailChanged,
                    text = email,
                    validityStates = isValidEmail
                )
                MainConfirmButton(
                    buttonTextID = R.string.forgot_password_button_text,
                    onButtonClicked = onButtonClicked,
                    modifier = Modifier.weight(3f),
                )
            }
            Spacer(modifier = Modifier.weight(1f, false))
        }
    }
}