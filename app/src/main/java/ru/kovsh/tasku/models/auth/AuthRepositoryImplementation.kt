package ru.kovsh.tasku.models.auth

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import ru.kovsh.tasku.models.auth.requestsDataClasses.AuthRequest
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult
import ru.kovsh.tasku.models.auth.requestsDataClasses.ForgotPasswordRequest
import ru.kovsh.tasku.models.auth.requestsDataClasses.NewPasswordRequest
import ru.kovsh.tasku.models.auth.requestsDataClasses.RefreshRequest


class AuthRepositoryImplementation(
    private val api: ru.kovsh.tasku.models.auth.AuthAPI,
    private val pref: SharedPreferences
) : ru.kovsh.tasku.models.auth.AuthRepository {
    private fun saveTokens(token: String, refresh: String) {
        pref.edit().putString("token", token).putString("refresh", refresh).apply()
    }

    override suspend fun authenticate(token: String): AuthResult<Unit> {
        return try {
            val result = api.authenticate(token)
            saveTokens(result.tokens.token, result.tokens.refresh)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.BadRequest()
                403 -> AuthResult.Forbidden()
                500 -> AuthResult.InternalServerError()
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    AuthResult.Error()
                }
            }
        } catch (e: Exception) {
            AuthResult.Error()
        }
    }

    override suspend fun newPassword(
        new_password: String,
        restore_refresh: String
    ): PasswordRecoveryResult<Unit> {
        return try {
            val tokenResponse = api.newPassword(
                request = NewPasswordRequest(
                    new_password = new_password,
                    restore_refresh = restore_refresh
                )
            )
            saveTokens(tokenResponse.tokens.token, tokenResponse.tokens.refresh)
            PasswordRecoveryResult.OK()
        } catch (e: HttpException) {
            Log.e("authentication", "http error code:" + e.code().toString())
            when (e.code()) {
                400 -> PasswordRecoveryResult.Bad_Request()
                404 -> PasswordRecoveryResult.Not_Found()
                500 -> PasswordRecoveryResult.Internal_Server_Error()
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    PasswordRecoveryResult.Error()
                }
            }
        } catch (e: Exception) {
            PasswordRecoveryResult.Error()
        }
    }

    override suspend fun restorePassword(email: String): PasswordRecoveryResult<Unit> {
        return try {
            api.restorePassword(request = ForgotPasswordRequest(email = email))
            PasswordRecoveryResult.OK()
        } catch (e: HttpException) {
            Log.e("authentication", "http error code:" + e.code().toString())
            when (e.code()) {
                400 -> PasswordRecoveryResult.Bad_Request()
                403 -> PasswordRecoveryResult.Forbidden()
                404 -> PasswordRecoveryResult.Not_Found()
                500 -> PasswordRecoveryResult.Internal_Server_Error() //Two requests per 12 hours
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    PasswordRecoveryResult.Error()
                }
            }
        } catch (e: Exception) {
            PasswordRecoveryResult.Error()
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            val tokenResponse =
                api.signIn(request = AuthRequest(email = email, pwd_hash = password))

            saveTokens(tokenResponse.tokens.token, tokenResponse.tokens.refresh)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.BadRequest()
                403 -> AuthResult.Forbidden()
                500 -> AuthResult.InternalServerError()
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    AuthResult.Error()
                }
            }
        } catch (e: Exception) {
            AuthResult.Error()
        }
    }

    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(request = AuthRequest(email = email, pwd_hash = password))
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.BadRequest()
                403 -> AuthResult.Forbidden()
                500 -> AuthResult.InternalServerError()
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    AuthResult.Error()
                }
            }
        } catch (e: Exception) {
            Log.e("Misunderstand", e.toString())
            AuthResult.Error()
        }
    }

    override suspend fun refresh(rToken: String): AuthResult<Unit> {
        return try {
            val tokenResponse = api.refresh(request = RefreshRequest(refresh_token = rToken))
            pref.edit().putString("token", tokenResponse.tokens.token)
                .putString("refresh", tokenResponse.tokens.refresh)
                .apply()
            Log.i("Tokens", "AuthRepoImplementation refresh: tokens successfully refreshed")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.BadRequest()
                500 -> AuthResult.InternalServerError()
                else -> {
                    Log.e(
                        "AuthRepositoryImpl", "refresh erorr code: "
                                + e.code().toString()
                    )
                    AuthResult.Error()
                }
            }
        } catch (e: Exception) {
            Log.e("Misunderstand", e.toString())
            AuthResult.Error()
        }
    }

    override suspend fun logOut(rToken: String): AuthResult<Unit> {
        return try {
            api.logOut(request = RefreshRequest(refresh_token = rToken))
            pref.edit().clear().apply()
            AuthResult.Unauthorized()
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> AuthResult.BadRequest()
                500 -> AuthResult.InternalServerError()
                else -> {
                    Log.e("AuthRepositoryImpl", e.code().toString())
                    AuthResult.Error()
                }
            }
        } catch (e: Exception) {
            AuthResult.Error()
        }
    }

}

