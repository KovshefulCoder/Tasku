package ru.kovsh.tasku.models.auth

import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult

interface AuthRepository {
    suspend fun authenticate(token: String): AuthResult<Unit>
    suspend fun newPassword(new_password: String, restore_refresh: String): PasswordRecoveryResult<Unit>
    suspend fun restorePassword(email: String): PasswordRecoveryResult<Unit>
    suspend fun signIn(email: String, password: String): AuthResult<Unit>
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun refresh(rToken: String): AuthResult<Unit>
    suspend fun logOut(rToken: String): AuthResult<Unit>
}