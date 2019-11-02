package com.hoopcarpool.archexample.core.utils

import timber.log.Timber

sealed class Resource<T> {

    class Success<T>(val value: T) : Resource<T>() {
        override fun toString(): String {
            return "Success = $value"
        }
    }
    class Empty<T> : Resource<T>() {
        override fun toString(): String {
            return "Empty"
        }
    }
    class Loading<T>(val value: T? = null) : Resource<T>() {
        override fun toString(): String {
            return "Loading = $value"
        }
    }
    class Failure<T>(val exception: Throwable? = null) : Resource<T>() {
        override fun toString(): String {
            return "Failure = $exception"
        }
    }

    /**
     * Log into timber a resource
     */
    fun logIt(tag: String) {

        Timber.tag(tag).d(
            " \n" +
                "┌────────────────────────────────────────────\n" +
                "├─> ${toString()}\n" +
                "└────────────────────────────────────────────"
        )
    }
}
