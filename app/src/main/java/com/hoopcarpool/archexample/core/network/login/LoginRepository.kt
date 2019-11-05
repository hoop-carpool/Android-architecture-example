package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.flux.RequestAuthAction
import com.hoopcarpool.archexample.core.flux.SessionStore
import com.hoopcarpool.archexample.core.utils.Resource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import mini.Dispatcher
import mini.rx.flowable
import mini.rx.select

interface LoginRepository {

    suspend fun doLogin(): Resource<LoginApi.Auth>
}

class LoginRepositoryImpl(
    private val dispatcher: Dispatcher,
    private val sessionStore: SessionStore
) : LoginRepository {

    override suspend fun doLogin(): Resource<LoginApi.Auth> {

        suspendCoroutine<Boolean> { continuation ->
            sessionStore.flowable()
                .select { it.tokenTask }
                .takeUntil { it.isTerminal }
                .subscribe {
                    if (it.isTerminal)
                        continuation.resume(true)
                }
            dispatcher.dispatchAsync(RequestAuthAction())
        }

        val token = sessionStore.state.token

        return if (token != null) {
            Resource.Success(token)
        } else {
            Resource.Failure(sessionStore.state.tokenTask.exceptionOrNull())
        }
    }
}
