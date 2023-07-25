package ru.kovsh.tasku.ui.mainMenu.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.theme.AccentBlue
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.PlaceholderText
import ru.kovsh.tasku.ui.theme.typography
import kotlin.math.roundToInt

@Composable
fun FAB(
    onFabClick: () -> Unit = {},
    onFabChange: (Float, Float) -> Unit,
    onFabStart: () -> Unit,
    onFabEnd: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
) {
    val fabX = remember { mutableStateOf(0f) }
    val fabY = remember { mutableStateOf(0f) }
    val xPosition = remember { mutableStateOf(0f) }
    val yPosition = remember { mutableStateOf(0f) }
    FloatingActionButton(
        onClick = onFabClick,
        modifier = modifier
            .padding(vertical = 8.dp)
            .size(72.dp)
            .offset { IntOffset(fabX.value.roundToInt(), fabY.value.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        onFabStart()
                    },
                    onDragEnd = {
                        fabX.value = 0f
                        fabY.value = 0f
                        xPosition.value = 0f
                        yPosition.value = 0f
                        onFabEnd()
                        focusRequester.requestFocus()
                    },
                ) { change, dragAmount ->
                    change.consume()
                    if (fabX.value + dragAmount.x < 0)
                        fabX.value += dragAmount.x
                    if (fabY.value + dragAmount.y < 0)
                        fabY.value += dragAmount.y
                }
            }
            .onGloballyPositioned { coordinates ->
                xPosition.value = coordinates.positionInRoot().x
                yPosition.value = coordinates.positionInRoot().y
                //Log.i("FABonGloballyPositioned", "x: $xPosition, y: $yPosition")
                //Log.i("FABonGloballyPositioned", "fabX: ${fabX.value}, fabY: ${fabY.value}")
                if (fabX.value == 0f && fabY.value == 0f)
                    onFabChange(0f, 0f)
                else
                    onFabChange(xPosition.value, yPosition.value)
            },
        backgroundColor = AccentBlue,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_fab),
            contentDescription = "FAB",
        )
    }
}

@Composable
fun TextFieldPlaceholderUnderFab(
    onAddNewArea: (String) -> Unit,
    height: Dp = 15.dp,
    focusRequester: FocusRequester
) {
    var placeholderDisplayed by remember { mutableStateOf(true) }
    val text = remember { mutableStateOf("") }
    val placeholder = when (text.value) {
        "" -> stringResource(id = R.string.placeholder_text_textfield_under_fab) + stringResource(id = R.string.ellipsis)
        else -> ""
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(DarkBlueBackground900),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            AreaImage(
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth(),
                areaName = text.value
            )
            BasicTextField(
                value = text.value,
                onValueChange = {
                    placeholderDisplayed = it.isEmpty()
                    text.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlueBackground900)
                    .weight(9f)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAddNewArea(text.value) }
                ),
                singleLine = true,
                cursorBrush = SolidColor(AccentBlue),
                textStyle = typography.subtitle2,
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
                    }
                }
            )
        }
    }
}
