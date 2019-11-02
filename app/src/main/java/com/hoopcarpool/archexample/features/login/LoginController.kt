package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.network.successOrThrow
import mini.Resource

interface LoginController {

    suspend fun doLogin(): Resource<Auth>
}

class LoginControllerImpl(private val loginApi: LoginApi) : LoginController {

    override suspend fun doLogin(): Resource<Auth> {
        return try {
            val auth = loginApi.oauthGetToken().successOrThrow()
            Resource.success(auth)
        } catch (exception: Exception) {
            Resource.failure(exception)
        }
    }
}
