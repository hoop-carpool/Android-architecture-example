package com.hoopcarpool.archexample.core.utils

import kotlinx.coroutines.Job

@Suppress("LeakingThis")
sealed class Task {

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> exception
            is Success, is Idle, is Loading -> null
        }

    val isTerminal: Boolean =
        when (this) {
            is Success, is Failure -> true
            is Idle, is Loading -> false
        }

    val isLoading: Boolean
        get() = this is Loading

    fun cancelJob() {
        if (this is Loading)
            job.cancel()
    }

    class Success : Task()
    class Idle : Task()
    class Loading(val job: Job) : Task()
    class Failure(val exception: Throwable? = null) : Task()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Idle -> "Idle"
            is Loading -> "Loading"
            is Failure -> "Failure($exception)"
        }
    }
}
