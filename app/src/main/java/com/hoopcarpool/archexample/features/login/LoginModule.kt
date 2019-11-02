package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.extensions.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

/**
 * Kodein module that provide login feature dependencies
 */
object LoginModule {

    fun create() = Kodein.Module("loginFeature", true) {
        bindViewModel<LoginViewModel>() with provider { LoginViewModel(instance()) }
        bind<LoginController>() with singleton { LoginControllerImpl(instance()) }
        bind<LoginApi>() with singleton { instance<Retrofit>().create(LoginApi::class.java) }
    }
}
