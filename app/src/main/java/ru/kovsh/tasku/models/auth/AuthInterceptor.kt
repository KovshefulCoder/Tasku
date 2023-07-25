package ru.kovsh.tasku.models.auth

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.kovsh.tasku.models.auth.requestsDataClasses.RefreshRequest
import ru.kovsh.tasku.models.auth.resultsClasses.AuthResult
import java.io.IOException
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val pref: SharedPreferences,
    private val authRepository: ru.kovsh.tasku.models.auth.AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = getToken() ?: throw ru.kovsh.tasku.models.auth.UnauthorizedException("User is not authorized")
        var requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
        var newRequest = requestBuilder.build()
        val response = chain.proceed(newRequest)
        if (response.code() == 401) {
            Log.i("AuthInterceptor", "Token expired, going to refresh")
            val rToken = getRefreshToken() ?: throw ru.kovsh.tasku.models.auth.UnauthorizedException(
                "User is not authorized"
            )
            when (runBlocking { authRepository.refresh(rToken) }) {
                is AuthResult.Authorized -> {
                    Log.i("AuthInterceptor", "Token refreshed, retrying request")
                    val newToken = getToken() ?: throw ru.kovsh.tasku.models.auth.UnauthorizedException(
                        "User is not authorized"
                    )
                    Log.i("AuthInterceptor", "New token: $newToken")
                    requestBuilder = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                    newRequest = requestBuilder.build()
                    Log.i("AuthInterceptor", "Going to retry request")
                    return chain.proceed(newRequest)
                }
                else -> {
                    Log.i("AuthInterceptor", "Token refresh failed, throwing exception")
                    throw ru.kovsh.tasku.models.auth.UnauthorizedException("User is not authorized")
                }
            }
        }
        return response
    }

    private fun getToken(): String? {
        return pref.getString("token", null)
    }

    private fun getRefreshToken(): String? {
        return pref.getString("refresh", null)
    }
}

class UnauthorizedException(message: String) : IOException(message)

class TokenExpiredException(message: String) : IOException(message)