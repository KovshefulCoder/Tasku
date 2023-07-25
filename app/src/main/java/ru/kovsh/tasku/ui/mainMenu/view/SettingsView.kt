package ru.kovsh.tasku.ui.mainMenu.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.UserAuthenticationState
import ru.kovsh.tasku.ui.mainMenu.viewModel.SettingsViewModel
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.typography

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClicked: () -> Unit,
    onUnauthorized: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    when (state.value.isAuthorized) {
        UserAuthenticationState.Unauthorized -> {
            viewModel.reset()
            onUnauthorized()
        }
        else -> {
            SettingsScreen(
                onLogOut = viewModel::onLogOut,
                onBackClicked = onBackClicked
            )
        }
    }
}

@Preview
@Composable
fun PrevSettingsScreen() {
    SettingsScreen(
        onLogOut = {},
        onBackClicked = {}
    )
}

@Composable
private fun SettingsScreen(
    onLogOut: () -> Unit,
    onBackClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_main_menu_settings),
                    contentDescription = "Settings",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Settings",
                    style = typography.h3.copy(color = Base500)
                )
            }
            Column(
                modifier = Modifier.weight(16f).clip(RoundedCornerShape(10.dp)),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().background(DarkBlueBackground900),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Button(
                        onClick = onLogOut,
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    ) {
                        Text(text = "Log out")
                    }
                }
            }
        }
    }
    BackHandler(onBack = onBackClicked)
}