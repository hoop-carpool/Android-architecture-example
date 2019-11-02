package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.network.successOrThrow
import com.hoopcarpool.archexample.core.utils.Resource

interface LoginController {

    suspend fun doLogin(): Resource<LoginApi.Auth>
}

class LoginControllerImpl(private val loginApi: LoginApi) : LoginController {

    override suspend fun doLogin(): Resource<LoginApi.Auth> =
        try {
            val auth = loginApi.oauthGetToken().successOrThrow()
            Resource.Success(auth)
        } catch (exception: Exception) {
            Resource.Failure<LoginApi.Auth>(exception)
        }.also { it.logIt(javaClass.name) }
}
