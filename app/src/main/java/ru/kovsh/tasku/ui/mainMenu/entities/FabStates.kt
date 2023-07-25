package ru.kovsh.tasku.ui.mainMenu.entities

import androidx.compose.ui.layout.LookaheadLayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class FabStates(
    val textFieldUnderFabIndex : Int = -2,
    val textFieldUnderFabHeight : Dp = 0.dp,
    val elementsSize : Pair<Int, Int> = Pair(-1, -1)
)
