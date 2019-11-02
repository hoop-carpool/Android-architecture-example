package com.hoopcarpool.archexample.features.login

interface LoginController {

    suspend fun doLogin()
}

class LoginControllerImpl : LoginController {

    override suspend fun doLogin() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
