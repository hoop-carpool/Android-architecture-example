package com.hoopcarpool.archexample.core.network.login

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hoopcarpool.archexample.core.utils.JobTask
import kotlinx.coroutines.*
import mini.Dispatcher
import retrofit2.HttpException

interface SessionController {
    fun saveAuth(auth: AuthApi.Auth)

    fun retrieveAuth(): AuthApi.Auth?

    fun doAuth(): Job
}

@Suppress("PrivatePropertyName")
class SessionControllerImpl(
    private val sharedPreferences: SharedPreferences,
    private val authApi: AuthApi,
    private val dispatcher: Dispatcher
) : SessionController {

    private val AUTH_ACCESS_TOKEN_KEY = "auth_access_token"
    private val AUTH_SCOPE_KEY = "auth_scope"
    private val AUTH_TOKEN_TYPE_KEY = "auth_token_type"

    override fun saveAuth(auth: AuthApi.Auth) {
        sharedPreferences.edit {
            putString(AUTH_ACCESS_TOKEN_KEY, auth.accessToken)
            putString(AUTH_SCOPE_KEY, auth.scope)
            putString(AUTH_TOKEN_TYPE_KEY, auth.tokenType)
        }
    }

    override fun retrieveAuth(): AuthApi.Auth? {
        val accessToken = sharedPreferences.getString(AUTH_ACCESS_TOKEN_KEY, null) ?: return null
        val scope = sharedPreferences.getString(AUTH_SCOPE_KEY, null) ?: return null
        val tokenType = sharedPreferences.getString(AUTH_TOKEN_TYPE_KEY, null) ?: return null

        return AuthApi.Auth(accessToken, scope, tokenType)
    }

    override fun doAuth() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val auth = authApi.oauthGetToken()
            dispatcher.dispatchAsync(RequestAuthCompletedAction(auth, JobTask.Success()))
        } catch (exception: HttpException) {
            dispatcher.dispatchAsync(
                RequestAuthCompletedAction(
                    null,
                    JobTask.Failure(exception)
                )
            )
        }
    }
}
