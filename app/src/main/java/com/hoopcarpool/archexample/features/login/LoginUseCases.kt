package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.network.login.AuthApi
import com.hoopcarpool.archexample.core.network.login.RequestAuthAction
import com.hoopcarpool.archexample.core.network.login.SessionStore
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.core.utils.waitForUntil
import java.io.IOException
import mini.Dispatcher
import mini.rx.SubscriptionTracker

interface LoginUseCases {

    suspend fun doLogin(subscriptionTracker: SubscriptionTracker): Resource<AuthApi.Auth>
}

class LoginUseCasesImpl(
    private val dispatcher: Dispatcher,
    private val sessionStore: SessionStore
) : LoginUseCases {

    override suspend fun doLogin(subscriptionTracker: SubscriptionTracker): Resource<AuthApi.Auth> {
        val token = subscriptionTracker.waitForUntil(
            sessionStore,
            trigger = { dispatcher.dispatchAsync(RequestAuthAction()) },
            condition = { it.tokenJobTask.isTerminal },
            select = { it.token }).await()

        return if (token != null) {
            Resource.Success(token)
        } else {
            Resource.Failure(sessionStore.state.tokenJobTask.exceptionOrNull())
        }
    }
}
