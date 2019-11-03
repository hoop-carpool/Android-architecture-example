package com.hoopcarpool.archexample.core.flux

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hoopcarpool.archexample.core.network.login.LoginApi

@Suppress("PrivatePropertyName")
class SessionController(private val sharedPreferences: SharedPreferences) {

    private val AUTH_ACCESS_TOKEN_KEY = "auth_access_token"
    private val AUTH_SCOPE_KEY = "auth_scope"
    private val AUTH_TOKEN_TYPE_KEY = "auth_token_type"

    fun saveAuth(auth: LoginApi.Auth) {
        sharedPreferences.edit {
            putString(AUTH_ACCESS_TOKEN_KEY, auth.accessToken)
            putString(AUTH_SCOPE_KEY, auth.scope)
            putString(AUTH_TOKEN_TYPE_KEY, auth.tokenType)
        }
    }

    fun retrieveAuth(): LoginApi.Auth? {
        val accessToken = sharedPreferences.getString(AUTH_ACCESS_TOKEN_KEY, null) ?: return null
        val scope = sharedPreferences.getString(AUTH_SCOPE_KEY, null) ?: return null
        val tokenType = sharedPreferences.getString(AUTH_TOKEN_TYPE_KEY, null) ?: return null

        return LoginApi.Auth(accessToken, scope, tokenType)
    }
}
