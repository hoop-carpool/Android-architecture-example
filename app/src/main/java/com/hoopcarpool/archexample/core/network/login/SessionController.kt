package com.hoopcarpool.archexample.core.network.login

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hoopcarpool.archexample.core.utils.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mini.Dispatcher
import retrofit2.HttpException
import timber.log.Timber

interface SessionController {
    fun saveAuth(auth: LoginApi.Auth)

    fun retrieveAuth(): LoginApi.Auth?

    fun doAuth(): Job
}

@Suppress("PrivatePropertyName")
class SessionControllerImpl(
    private val sharedPreferences: SharedPreferences,
    private val loginApi: LoginApi,
    private val dispatcher: Dispatcher
) : SessionController {

    private val AUTH_ACCESS_TOKEN_KEY = "auth_access_token"
    private val AUTH_SCOPE_KEY = "auth_scope"
    private val AUTH_TOKEN_TYPE_KEY = "auth_token_type"

    override fun saveAuth(auth: LoginApi.Auth) {
        sharedPreferences.edit {
            putString(AUTH_ACCESS_TOKEN_KEY, auth.accessToken)
            putString(AUTH_SCOPE_KEY, auth.scope)
            putString(AUTH_TOKEN_TYPE_KEY, auth.tokenType)
        }
    }

    override fun retrieveAuth(): LoginApi.Auth? {
        val accessToken = sharedPreferences.getString(AUTH_ACCESS_TOKEN_KEY, null) ?: return null
        val scope = sharedPreferences.getString(AUTH_SCOPE_KEY, null) ?: return null
        val tokenType = sharedPreferences.getString(AUTH_TOKEN_TYPE_KEY, null) ?: return null

        return LoginApi.Auth(accessToken, scope, tokenType)
    }

    override fun doAuth() =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val auth = loginApi.oauthGetToken()
                dispatcher.dispatchAsync(RequestAuthCompletedAction(auth, Task.Success()))
            } catch (exception: HttpException) {
                dispatcher.dispatchAsync(
                    RequestAuthCompletedAction(
                        null,
                        Task.Failure(exception)
                    )
                )
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
}
