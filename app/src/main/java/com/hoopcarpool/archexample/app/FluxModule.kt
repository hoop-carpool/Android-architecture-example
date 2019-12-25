package com.hoopcarpool.archexample.app

import com.hoopcarpool.archexample.core.network.session.SessionController
import com.hoopcarpool.archexample.core.network.session.SessionControllerImpl
import com.hoopcarpool.archexample.core.network.session.SessionStore
import mini.kodein.bindStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Kodein module that provides flux dependencies
 */
object FluxModule {
    fun create() = Kodein.Module("flux", true) {
        bindStore { SessionStore(instance()) }
        bind<SessionController>() with singleton { SessionControllerImpl(instance(), instance(), instance()) }
    }
}
