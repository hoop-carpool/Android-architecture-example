package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.network.session.AuthApi
import com.hoopcarpool.archexample.core.network.session.RequestAuthAction
import com.hoopcarpool.archexample.core.network.session.SessionStore
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.core.utils.waitForUntil
import kotlinx.coroutines.FlowPreview
import mini.Dispatcher
import mini.rx.SubscriptionTracker

interface LoginUseCases {

    suspend fun doLogin(subscriptionTracker: SubscriptionTracker): Resource<AuthApi.Auth>
}

class LoginUseCasesImpl(
    private val dispatcher: Dispatcher,
    private val sessionStore: SessionStore
) : LoginUseCases {

    @FlowPreview
    override suspend fun doLogin(subscriptionTracker: SubscriptionTracker): Resource<AuthApi.Auth> {

        val token = subscriptionTracker.waitForUntil(
            sessionStore,
            { dispatcher.dispatchAsync(RequestAuthAction()) },
            { it.tokenJobTask.isTerminal },
            { it.token }
        ).await()

        return if (token != null) {
            Resource.Success(token)
        } else {
            Resource.Failure(sessionStore.state.tokenJobTask.exceptionOrNull())
        }
    }
}
