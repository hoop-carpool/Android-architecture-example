package com.hoopcarpool.archexample.core.utils

import kotlinx.coroutines.Job

@Suppress("LeakingThis")
sealed class Task {

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> exception
            Success, Idle, is Loading -> null
        }

    val isTerminal: Boolean =
        when (this) {
            Success, is Failure -> true
            Idle, is Loading -> false
        }

    val isLoading: Boolean
        get() = this is Loading

    object Success : Task()
    object Idle : Task()
    class Loading(val job: Job) : Task()
    class Failure(val exception: Throwable? = null) : Task()

}
