package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.flux.RequestAuthCompletedAction
import com.hoopcarpool.archexample.core.network.successOrThrow
import com.hoopcarpool.archexample.core.utils.Resource
import mini.Dispatcher

interface LoginRepository {

    suspend fun doLogin(): Resource<LoginApi.Auth>
}

class LoginRepositoryImpl(
    private val dispatcher: Dispatcher,
    private val loginApi: LoginApi
) : LoginRepository {

    override suspend fun doLogin(): Resource<LoginApi.Auth> =
        try {
            val auth = loginApi.oauthGetToken().successOrThrow()
            dispatcher.dispatchAsync(RequestAuthCompletedAction(auth))
            Resource.Success(auth)
        } catch (exception: Exception) {
            Resource.Failure<LoginApi.Auth>(exception)
        }.also { it.logIt(javaClass.name) }
}
