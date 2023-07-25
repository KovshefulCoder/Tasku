package ru.kovsh.tasku.ui.auth.entities.sharedStates

import ru.kovsh.tasku.R

data class AuthDialog(
    val iconID: Int = R.drawable.email_sent_icon,
    val title: String = "",
    val text: String = ""
)
