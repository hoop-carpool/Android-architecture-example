package com.hoopcarpool.archexample.core.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomLoggerInterceptor : Interceptor {

    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
