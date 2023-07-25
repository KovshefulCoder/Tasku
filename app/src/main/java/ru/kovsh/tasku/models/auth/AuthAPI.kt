package ru.kovsh.tasku.models.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.kovsh.tasku.models.auth.requestsDataClasses.AuthRequest
import ru.kovsh.tasku.models.auth.resultsClasses.TokenResponse
import ru.kovsh.tasku.models.auth.requestsDataClasses.ForgotPasswordRequest
import ru.kovsh.tasku.models.auth.requestsDataClasses.NewPasswordRequest
import ru.kovsh.tasku.models.auth.requestsDataClasses.RefreshRequest


interface AuthAPI {
    @GET("auth/confirm/{confirm_token}")
    suspend fun authenticate(@Path("confirm_token") token: String): TokenResponse
    @POST("auth/new_password")
    suspend fun newPassword(@Body request: NewPasswordRequest) : TokenResponse
    @POST("auth/restore_password")
    suspend fun restorePassword(@Body request: ForgotPasswordRequest)
    @POST("auth/signin")
    suspend fun signIn(@Body request: AuthRequest): TokenResponse
    @POST("auth/signup")
    suspend fun signUp(@Body request: AuthRequest)
    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest) : TokenResponse
    @POST("auth/logout")
    suspend fun logOut(@Body request: RefreshRequest)
}