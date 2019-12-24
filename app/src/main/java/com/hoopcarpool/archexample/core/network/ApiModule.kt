package com.hoopcarpool.archexample.core.network

import com.hoopcarpool.archexample.core.network.login.AuthApi
import com.hoopcarpool.archexample.features.login.LoginUseCases
import com.hoopcarpool.archexample.features.login.LoginUseCasesImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object ApiModule {
    fun create() = Kodein.Module("api", true) {
        bind<LoginUseCases>() with singleton {
            LoginUseCasesImpl(
                instance(),
                instance()
            )
        }
        bind<AuthApi>() with singleton { instance<Retrofit>().create(AuthApi::class.java) }
    }
}
