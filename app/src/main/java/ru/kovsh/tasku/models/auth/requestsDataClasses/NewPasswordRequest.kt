package ru.kovsh.tasku.models.auth.requestsDataClasses

data class NewPasswordRequest(
    val new_password: String  = "",
    val restore_refresh : String = ""
)
