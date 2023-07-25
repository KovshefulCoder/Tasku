package ru.kovsh.tasku.ui.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.kovsh.tasku.ui.auth.views.Logo
import ru.kovsh.tasku.ui.theme.Background
import ru.kovsh.tasku.ui.theme.Base0


@Preview
@Composable
fun PrevMainScreen() {
    MainScreen()
}

@Composable
internal fun MainScreen(modifier: Modifier = Modifier) {
    MainScreen()
}


@Composable
private fun MainScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            //.background(Background.copy(alpha = 0.5f))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp),
                    //.zIndex(2f),
                color = Base0,
                strokeWidth = 5.dp
            )
        }
    }
}