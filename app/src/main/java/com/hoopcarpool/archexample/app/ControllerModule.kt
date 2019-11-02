package com.hoopcarpool.archexample.app

import com.hoopcarpool.archexample.features.login.LoginController
import com.hoopcarpool.archexample.features.login.LoginControllerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * Kodein module that provide viewmodels
 */
object ControllerModule {
    fun create() = Kodein.Module("controller", true) {
        bind<LoginController>() with singleton { LoginControllerImpl() }
    }
}
