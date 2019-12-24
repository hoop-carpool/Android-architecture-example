package com.hoopcarpool.archexample.core.utils

import java.util.concurrent.CancellationException
import kotlinx.coroutines.Job

@Suppress("LeakingThis")
sealed class JobTask {

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

    fun cancelJob(msg: String? = null) {
        if (this is Loading && job.isActive) job.cancel(CancellationException(msg))
    }

    class Success : JobTask()
    class Idle : JobTask()
    class Loading(val job: Job) : JobTask()
    class Failure(val exception: Throwable? = null) : JobTask()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Idle -> "Idle"
            is Loading -> "Loading"
            is Failure -> "Failure($exception)"
        }
    }
}
