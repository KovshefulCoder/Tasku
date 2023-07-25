package ru.kovsh.tasku.ui.mainMenu.entities

import ru.kovsh.tasku.R

data class MenuCategory(
    val id: Int,
    val nameID: Int,
    val iconID: Int,
    val tasksCounter: Int
)

val menuCategories = listOf(
    MenuCategory(
        id = 0,
        nameID = R.string.main_menu_inbox,
        iconID = R.drawable.ic_main_menu_inbox,
        tasksCounter = 0
    ),
    MenuCategory(
        id = 1,
        nameID = R.string.main_menu_all,
        iconID = R.drawable.ic_main_menu_all,
        tasksCounter = 0
    ),
    MenuCategory(
        id = 2,
        nameID = R.string.main_menu_today,
        iconID = R.drawable.ic_main_menu_today,
        tasksCounter = 1
    ),
    MenuCategory(
        id = 3,
        nameID = R.string.main_menu_upcoming,
        iconID = R.drawable.ic_main_menu_upcoming,
        tasksCounter = 11
    ),
    MenuCategory(
        id = 4,
        nameID = R.string.main_menu_someday,
        iconID = R.drawable.ic_main_menu_someday,
        tasksCounter = 0
    ),
    MenuCategory(
        id = 5,
        nameID = R.string.main_menu_journal,
        iconID = R.drawable.ic_main_menu_journal,
        tasksCounter = 0
    )
)