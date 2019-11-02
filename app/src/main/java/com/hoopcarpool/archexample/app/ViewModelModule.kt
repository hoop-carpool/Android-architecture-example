package com.hoopcarpool.archexample.app

import com.hoopcarpool.archexample.core.base.BaseViewModel
import com.hoopcarpool.archexample.features.login.LoginViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * Kodein module that provide viewmodels
 */
object ViewModelModule {
    fun create() = Kodein.Module("viewmodel", true) {
        bindViewModel<LoginViewModel>() with provider { LoginViewModel(instance()) }
    }

    private inline fun <reified T : BaseViewModel> Kodein.Builder.bindViewModel(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
        return bind<T>(T::class.java.simpleName, overrides)
    }
}
