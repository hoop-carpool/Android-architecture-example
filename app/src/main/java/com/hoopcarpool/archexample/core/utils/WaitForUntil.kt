package com.hoopcarpool.archexample.core.utils

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CompletableDeferred
import mini.Store
import mini.rx.SubscriptionTracker
import mini.rx.flowable

inline fun <T, U> SubscriptionTracker.waitForUntil(
    store: Store<T>,
    crossinline trigger: () -> Unit,
    crossinline condition: (T) -> Boolean,
    crossinline select: (T) -> U
): CompletableDeferred<U> {

    trigger()

    val deferred = CompletableDeferred<U>()

    var disposable: Disposable? = null
    disposable = store.flowable(hotStart = false)
        .takeUntil { condition(it) }
        .subscribe {
            if (condition(it)) {
                disposable?.dispose()
                deferred.complete(select(store.state))
            }
        }.track()

    return deferred
}
