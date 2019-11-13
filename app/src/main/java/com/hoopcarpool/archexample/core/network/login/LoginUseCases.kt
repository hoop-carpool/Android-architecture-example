package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.utils.Resource
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CompletableDeferred
import mini.Dispatcher
import mini.Store
import mini.rx.flowable

interface LoginUseCases {

    suspend fun doLogin(): Resource<LoginApi.Auth>
}

class LoginUseCasesImpl(
    private val dispatcher: Dispatcher,
    private val sessionStore: SessionStore
) : LoginUseCases {

    override suspend fun doLogin(): Resource<LoginApi.Auth> {
        return try {

            val token = waitForUntil(sessionStore,
                trigger = { dispatcher.dispatchAsync(RequestAuthAction()) },
                condition = { it.tokenTask.isTerminal },
                select = { it.token }).await()

            return if (token != null) {
                Resource.Success(token)
            } else {
                Resource.Failure(sessionStore.state.tokenTask.exceptionOrNull())
            }
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}

inline fun <T, U> waitForUntil(
    store: Store<T>,
    crossinline trigger: () -> Unit,
    crossinline condition: (T) -> Boolean,
    crossinline select: (T) -> U
): CompletableDeferred<U> {

    trigger()

    var disposable: Disposable? = null
    val deferred = CompletableDeferred<U>()
    disposable = store.flowable()
        .subscribe {
            if (condition(it)) {
                if (disposable?.isDisposed == false) {
                    disposable?.dispose()
                }
                deferred.complete(select(store.state))
            }
        }

    return deferred
}
