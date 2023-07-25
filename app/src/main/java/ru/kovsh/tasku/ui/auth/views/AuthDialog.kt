package ru.kovsh.tasku.ui.auth.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthDialog
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.signInUpStates.AuthScreenStates
import ru.kovsh.tasku.ui.theme.*

@Composable
fun StatusDialogAuth(
    onSuccessfulAuth: () -> Unit,
    onDialogBackClicked: () -> Unit,
    authScreenState: AuthScreenStates,
    authRequestState: AuthRequestsStates,
    email: String,
) {
    when (authRequestState) {
        is AuthRequestsStates.Success -> {
            when (authScreenState) {
                is AuthScreenStates.SignUp -> {
                    StatusDialog(
                        onDialogBackClicked,
                        AuthDialog(
                            iconID = R.drawable.inbox_icon,
                            title = stringResource(id = R.string.success_signup_title) + " $email",
                            text = stringResource(id = R.string.success_signup_text)
                        )
                    )
                }
                is AuthScreenStates.SignIn, AuthScreenStates.Authentication ->
                {
                    onSuccessfulAuth()
                }
                else -> {
                }
            }
        }
        is AuthRequestsStates.LoadingEmailConfirmation -> {
            when (authScreenState) {
                is AuthScreenStates.SignUp -> {
                    StatusDialog(
                        onDialogBackClicked,
                        AuthDialog(
                            iconID = R.drawable.inbox_icon,
                            title = "Checking email $email...",
                            text = "Fun fact: I spent 2 entire days to make this check work"
                        )
                    )
                }
                else -> {/*TODO*/
                }
            }
        }
        else -> {/*TODO*/
        }
    }
}

@Composable
fun StatusDialog(
    onDialogBackClicked: () -> Unit,
    authDialog: AuthDialog,
) {
    Dialog(
        onDismissRequest = { /*TODO*/ },
        content = {
            Card(
                //TODO() - try to remove Card
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Background,
                elevation = 0.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                    {
                        Icon(
                            painter = painterResource(id = authDialog.iconID),
                            contentDescription = "Status icon",
                            tint = AccentBlue,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (authDialog.title != "") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = authDialog.title,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            style = typography.h4,
                            color = Base0,
                        )
                    }
                    if (authDialog.text != "") {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = authDialog.text,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            style = typography.body1,
                            color = Base500
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onDialogBackClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AccentBlue,
                            contentColor = Base0
                        ),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(text = "Back", style = typography.button)
                    }
                }
            }
        },
    )
}
