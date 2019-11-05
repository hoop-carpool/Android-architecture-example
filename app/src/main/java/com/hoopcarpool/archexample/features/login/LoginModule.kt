package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.extensions.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * Kodein module that provide login feature dependencies
 */
object LoginModule {

    fun create() = Kodein.Module("loginFeature", true) {
        bindViewModel<LoginViewModel>() with provider { LoginViewModel(instance()) }
    }
}
