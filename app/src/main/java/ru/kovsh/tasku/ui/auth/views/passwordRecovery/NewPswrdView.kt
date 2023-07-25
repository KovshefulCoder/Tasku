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
import ru.kovsh.tasku.ui.auth.viewModels.passwordRecovery.NewPswrdViewModel
import ru.kovsh.tasku.ui.auth.views.Logo
import ru.kovsh.tasku.ui.auth.views.MainConfirmButton
import ru.kovsh.tasku.ui.auth.views.PasswordTextField
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.typography

@Composable
internal fun NewPasswordView(
    onSuccessfulAuth : () -> Unit,
    viewModel: NewPswrdViewModel,
) {
    val state by viewModel.state.collectAsState()
    if (state.authResult == AuthRequestsStates.Success) {
        onSuccessfulAuth()
    }
    NewPasswordView(
        password = state.password,
        repeatedPassword = state.repeatedPassword,
        isValidPassword = state.isValidPassword,
        isValidRepeatedPassword = state.isValidRepeatedPassword,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatedPasswordChange = viewModel::onRepeatedPasswordChange,
        onPasswordSubmit = viewModel::onPasswordSubmit,

        )
}

@Preview(backgroundColor = 0xFF17171A)
@Composable
fun PrevNewPasswordView() {
    NewPasswordView(
        password = "",
        repeatedPassword = "",
        isValidPassword = ValidityStates.Valid,
        isValidRepeatedPassword = ValidityStates.Valid,
        onPasswordChange = {},
        onRepeatedPasswordChange = {},
        onPasswordSubmit = {},
    )
}


@Composable
private fun NewPasswordView(
    password: String,
    repeatedPassword: String,
    isValidPassword: ValidityStates,
    isValidRepeatedPassword: ValidityStates,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    onPasswordSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Logo(modifier = Modifier.weight(3f, false))
            Text(
                modifier = Modifier.weight(1f, false),
                text = stringResource(id = R.string.password_recovery_title),
                textAlign = TextAlign.Center,
                color = Base0,
                style = typography.h2,
            )
            Text(
                modifier = Modifier.weight(1f, false),
                text = stringResource(id = R.string.new_password_title),
                textAlign = TextAlign.Center,
                color = Base500,
                style = typography.h4.copy(
                    fontWeight = FontWeight.Normal
                ),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                )
                {
                    PasswordTextField(
                        onPasswordChanged = onPasswordChange,
                        text = password,
                        validityStates = isValidPassword,
                        errorTextID = R.string.password_invalid,
                    )
                    PasswordTextField(
                        onPasswordChanged = onRepeatedPasswordChange,
                        text = repeatedPassword,
                        validityStates = isValidRepeatedPassword,
                        errorTextID = R.string.repeated_password_invalid,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, false),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                )
                {
                    MainConfirmButton(
                        buttonTextID = R.string.new_password_button_text,
                        onButtonClicked = onPasswordSubmit,
                        modifier = Modifier.weight(3f),
                    )
                }
                Spacer(modifier = Modifier.weight(1f, false))
            }
        }
    }
}