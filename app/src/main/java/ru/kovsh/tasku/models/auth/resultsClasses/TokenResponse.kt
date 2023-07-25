package ru.kovsh.tasku.models.auth.resultsClasses

import android.media.session.MediaSessionManager.OnSession2TokensChangedListener

data class TokenResponse(
    val tokens: ru.kovsh.tasku.models.auth.resultsClasses.TokenResponse.Tokens
) {
    data class Tokens(
        val refresh: String,
        val token: String
    )
}
