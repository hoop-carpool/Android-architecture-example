package com.hoopcarpool.archexample.core.extensions

import mini.Resource
import timber.log.Timber

/**
 * Log into timber a resource
 */
fun <T> Resource<T>.logIt(tag: String) {

    val msg = when {
        isSuccess -> "Success -> ${toString()}"
        isLoading -> "Loading -> ${toString()}"
        isFailure -> "isFailure -> ${toString()}"
        isEmpty -> "isEmpty -> ${toString()}"
        else -> toString()
    }
    Timber.tag(tag).d(
        " \n" +
        "┌────────────────────────────────────────────\n" +
        "├─> $msg\n" +
        "└────────────────────────────────────────────")
}
