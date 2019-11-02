package com.hoopcarpool.archexample.core.network

import retrofit2.HttpException
import retrofit2.Response

/** Execute callback with a non null body if the [Response] is successful, other way throw an [HttpException] */
@Throws(HttpException::class) fun <T> Response<T>.successOrThrow(): T {
    if (isSuccessful && body() != null) return body()!!
    else throw HttpException(this)
}

/** Execute callback if the [Response] is successful, other way throw an [HttpException] */
@Throws(HttpException::class)
inline fun <T> Response<T>.successOrThrow(crossinline success: () -> Unit) {
    if (isSuccessful && body() != null) success()
    else throw HttpException(this)
}
