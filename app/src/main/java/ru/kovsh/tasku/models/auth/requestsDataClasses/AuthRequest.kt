package ru.kovsh.tasku.models.auth.requestsDataClasses

data class AuthRequest(
    val email: String,
    val pwd_hash: String
)