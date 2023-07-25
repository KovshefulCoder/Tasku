package ru.kovsh.tasku.ui.auth.views

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.signInUpStates.AuthScreenStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates
import ru.kovsh.tasku.ui.auth.viewModels.AuthScreenViewModel
import ru.kovsh.tasku.ui.theme.*
import ru.kovsh.tasku.ui.theme.typography


//TODO(When switching to a SignUp screen from SignIn everything up to the new password field
// remained in place, and everything below shifted. If goes back back in the same order)
//TODO(Implement features):
// [✓] 1. Add a password validation
// [✓] 2. Add a password visibility toggle
// [✓] 3. Add a email validation
// [✓] 4. Password and repeated password should be the same
// 5. Handle requests and errors in dialogs
// 6. Loading status of signin and add to dialog of email confirm
// 7. Handle back button from bottom bar
// 8. Make main screen of auth with Modifier.weight()
// 9. Animate screen in forgot password screen to be able to press button from keyboard
// Optional:
// 1. Add animation of switches between screens
// 2. Discuss design async (different between ios and web)


@Composable
internal fun AuthenticationScreen(
    viewModel: AuthScreenViewModel,
    onSuccessfulAuth: () -> Unit,
    onForgotPassword: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    if (state.authScreenState == AuthScreenStates.ForgotPassword) {
        val encodedEmail = Uri.encode(state.email)
        viewModel.onClearViewModel()
        onForgotPassword(encodedEmail)
    }

    AuthenticationScreen(
        onSuccessfulAuth =
        {
            viewModel.onClearViewModel()
            onSuccessfulAuth()
        },
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onRepeatedPasswordChanged = viewModel::onRepeatedPasswordChanged,
        onBottomButtonClicked = viewModel::onBottomButtonClicked,
        onAuthButtonClicked = {
            Log.i("AuthScreen", "onAuthButtonClicked")
            viewModel.onAuthButtonClicked()
        },
        onDialogBackClicked = viewModel::onDialogBackClicked,
        onForgotPasswordClicked = viewModel::onForgotPasswordClicked,
        email = state.email,
        password = state.password,
        repeatedPassword = state.repeatedPassword,
        isValidEmail = state.isValidEmail,
        isValidPassword = state.isValidPassword,
        isValidRepeatedPassword = state.isValidRepeatedPassword,
        authScreenState = state.authScreenState,
        authRequestState = state.authResult,
        buttonTextID = state.buttonTextID,
        bottomButtonDescriptionID = state.bottomButtonDescriptionID,
        bottomButtonTextID = state.bottomButtonTextID,
        Modifier
    )
}

@Composable
private fun AuthenticationScreen(
    onSuccessfulAuth: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRepeatedPasswordChanged: (String) -> Unit,
    onBottomButtonClicked: () -> Unit,
    onAuthButtonClicked: () -> Unit,
    onDialogBackClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    email: String,
    password: String,
    repeatedPassword: String,
    isValidEmail: ValidityStates, //TODO()
    isValidPassword: ValidityStates,
    isValidRepeatedPassword: ValidityStates,
    authScreenState: AuthScreenStates,
    authRequestState: AuthRequestsStates,
    buttonTextID: Int,
    bottomButtonDescriptionID: Int,
    bottomButtonTextID: Int,
    modifier: Modifier = Modifier,
) {
    StatusDialogAuth(
        onSuccessfulAuth,
        onDialogBackClicked,
        authScreenState,
        authRequestState,
        email
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Logo()
        Spacer(modifier = Modifier.height(48.dp))
        EmailTextField(onEmailChanged, email, isValidEmail)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            onPasswordChanged,
            password,
            isValidPassword,
            errorTextID = R.string.password_invalid
        )
        ForgotPasswordButton(onForgotPasswordClicked, authScreenState)
        if (authScreenState == AuthScreenStates.SignUp) { //TODO(Fix)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                onRepeatedPasswordChanged,
                repeatedPassword,
                isValidRepeatedPassword,
                errorTextID = R.string.repeated_password_invalid
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        MainConfirmButton(buttonTextID = buttonTextID, onButtonClicked = onAuthButtonClicked)
        Spacer(modifier = Modifier.height(16.dp))
        GoogleAuthButton()
        Spacer(modifier = Modifier.height(80.dp))
        AuthBottomButton(
            onBottomClick = onBottomButtonClicked,
            bottomButtonDescriptionAbove = stringResource(id = bottomButtonDescriptionID),
            bottomButtonText = stringResource(id = bottomButtonTextID)
        )
    }
}

@Composable
fun Logo(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "app logo"
        )
        Spacer(modifier = modifier.height(24.dp))
        Text(
            modifier = modifier,
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            style = typography.h1,
            color = AccentBlue
        )
    }
}


@Composable
fun ForgotPasswordButton(
    onForgotPasswordClick: () -> Unit,
    authScreenState: AuthScreenStates,
    modifier: Modifier = Modifier
) {
    if (authScreenState == AuthScreenStates.SignIn) {
        TaskuTheme {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = {
                        onForgotPasswordClick()
                    },
                )
                {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = typography.caption,
                        color = AccentBlue
                    )
                }
            }
        }
    }
}

@Composable
fun MainConfirmButton(
    buttonTextID: Int?,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClicked,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = buttonColors(backgroundColor = AccentBlue, contentColor = Base0),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(text = stringResource(buttonTextID ?: R.string.ellipsis), style = typography.button)
    }
}

@Composable
fun GoogleAuthButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(10.dp),
        colors = buttonColors(backgroundColor = Transparent, contentColor = Base0),
        border = BorderStroke(1.dp, AccentBlue)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = stringResource(R.string.google_auth_button),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.google_auth_button),
                style = typography.button, color = Base0
            )
        }
    }
}

@Composable
fun AuthBottomButton(
    onBottomClick: () -> Unit,
    bottomButtonDescriptionAbove: String?,
    bottomButtonText: String?,
    modifier: Modifier = Modifier
) {
    Text(text = bottomButtonDescriptionAbove ?: "", style = typography.body1, color = Base700)
    TextButton(onClick = {
        onBottomClick()
    }) {
        Text(text = bottomButtonText ?: "Error", style = typography.button, color = Base0)
    }

}

@Composable
fun EmailTextField(
    onEmailChanged: (String) -> Unit = {},
    text: String = "",
    validityStates: ValidityStates,
    modifier: Modifier = Modifier
) {
    var placeholderDisplayed by remember { mutableStateOf(true) }
    val placeholder = when (text) {
        "" -> stringResource(id = R.string.auth_email_placeholder) + stringResource(id = R.string.ellipsis)
        else -> ""
    }
    val borderColor = if (validityStates == ValidityStates.Invalid ||
        validityStates == ValidityStates.Empty
    ) {
        Error
    } else {
        AccentBlue
    }
    TaskuTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    onEmailChanged(it)
                    placeholderDisplayed = it.isEmpty()
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp)
                    .fillMaxWidth(),
                textStyle = typography.body1,
                singleLine = true,
                cursorBrush = SolidColor(AccentBlue),
                decorationBox = { innerTextField ->
                    if (placeholderDisplayed) {
                        Text(
                            text = placeholder,
                            style = typography.body1.copy(
                                color = PlaceholderText,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                    innerTextField()
                }
            )
            if (borderColor == Error) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text(
                        text = "Invalid email",
                        style = typography.caption.copy(
                            color = borderColor
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun PasswordTextField(
    onPasswordChanged: (String) -> Unit = {},
    text: String = "",
    validityStates: ValidityStates,
    modifier: Modifier = Modifier,
    errorTextID: Int
) {
    var placeholderDisplayed by remember { mutableStateOf(true) }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val placeholderTextID = if (errorTextID == R.string.password_invalid) {
        R.string.auth_password_placeholder
    } else {
        R.string.auth_repeat_password_placeholder
    }
    val placeholder = when (text) {
        "" -> stringResource(id = placeholderTextID) + stringResource(id = R.string.ellipsis)
        else -> ""
    }
    val borderColor = if (validityStates == ValidityStates.Invalid ||
        validityStates == ValidityStates.Empty
    ) {
        Error
    } else {
        AccentBlue
    }
    TaskuTheme {
        BasicTextField(
            value = text,
            onValueChange = {
                onPasswordChanged(it)
                placeholderDisplayed = it.isEmpty()
            },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp)
                .fillMaxWidth(),
            textStyle = typography.body1,
            singleLine = true,
            cursorBrush = SolidColor(AccentBlue),
            visualTransformation = if (!isPasswordVisible.value)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (placeholderDisplayed) {
                            Text(
                                text = placeholder,
                                style = typography.body1.copy(
                                    color = PlaceholderText,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                        }
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = { isPasswordVisible.value = !isPasswordVisible.value },
                        modifier = Modifier.size(23.dp)
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(
                                id = if (!isPasswordVisible.value)
                                    R.drawable.ic_show_password
                                else
                                    R.drawable.ic_don_t_show_password
                            ),
                            contentDescription = "Show password",
                            tint = AccentBlue,
                        )
                    }
                }
            }
        )
        if (borderColor == Error) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(id = errorTextID),
                    style = typography.caption.copy(
                        color = borderColor
                    )
                )
            }
        }
    }
}