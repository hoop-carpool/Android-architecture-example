package com.hoopcarpool.archexample.app

import com.hoopcarpool.archexample.BuildConfig
import com.hoopcarpool.archexample.core.network.CustomHttpLoggerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

/**
 * Kodein module that provide network classes
 */
object NetworkModule {
    fun create() = Kodein.Module("network", true) {
        bind<OkHttpClient>() with singleton {
            val client = getOkHttpBuilder(60)
            val interceptor = CustomHttpLoggerInterceptor { message -> Timber.tag("OkHttpClient").d(message) }
                .setLevel(
                    if (BuildConfig.DEBUG)
                        CustomHttpLoggerInterceptor.Level.BODY
                    else
                        CustomHttpLoggerInterceptor.Level.NONE
                )
            client.addNetworkInterceptor(interceptor)

            client.build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .baseUrl(HttpUrl.parse("https://bhagavadgita.io/api/v1/")!!)
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<Moshi>() with singleton {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }
    }

    private fun getOkHttpBuilder(timeoutSeconds: Long) =
        OkHttpClient.Builder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
}
