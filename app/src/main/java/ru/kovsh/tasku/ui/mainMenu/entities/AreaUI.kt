package ru.kovsh.tasku.ui.mainMenu.entities

import androidx.compose.ui.graphics.Color
import ru.kovsh.tasku.R
data class AreaUI(
    val id: Long = -1,
    val name: String = "",
    val color: Color = Color.Transparent,
    val iconID: Int = R.drawable.ic_main_menu_area,
)
