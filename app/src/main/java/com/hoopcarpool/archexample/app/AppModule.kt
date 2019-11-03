package com.hoopcarpool.archexample.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import mini.Dispatcher
import mini.MiniGen
import mini.Store
import mini.kodein.android.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton

/**
 * Kodein module that provides app dependencies
 */
object AppModule {
    fun create() = Kodein.Module("app", true) {
        bind<Application>() with singleton { app }
        bind<Dispatcher>() with singleton { MiniGen.newDispatcher() }
        bind<SharedPreferences>() with singleton { instance<Context>().getSharedPreferences("hoop", Context.MODE_PRIVATE) }

        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein.direct) }

        bind() from setBinding<Store<*>>()
    }
}
