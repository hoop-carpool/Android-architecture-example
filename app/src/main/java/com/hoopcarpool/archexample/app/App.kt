package com.hoopcarpool.archexample.app

import android.app.Application
import androidx.annotation.RestrictTo
import com.facebook.stetho.Stetho
import com.hoopcarpool.archexample.BuildConfig
import com.hoopcarpool.archexample.core.network.ApiModule
import com.hoopcarpool.archexample.features.login.LoginModule
import java.io.Closeable
import kotlin.properties.Delegates
import mini.Dispatcher
import mini.LoggerInterceptor
import mini.MiniGen
import mini.Store
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

class App : Application(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)

    private var testModule: Kodein.Module? = null

    private lateinit var dispatcher: Dispatcher
    private lateinit var stores: List<Store<*>>
    private lateinit var storeSubscriptions: Closeable

    override fun onCreate() {
        super.onCreate()

        appInstance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        // Start dependency injection
        initializeInjection()
    }

    /**
     * Initializes dependency injection.
     */
    fun initializeInjection() {
        // Clear everything
        if (this::storeSubscriptions.isInitialized) {
            storeSubscriptions.close()
        }

        if (this::stores.isInitialized) {
            stores.forEach { it.close() }
        }

        kodein.clear()

        with(kodein) {
            addImport(AppModule.create(), false)
            addImport(LoginModule.create(), true)
            addImport(NetworkModule.create(), false)
            addImport(FluxModule.create(), false)
            addImport(ApiModule.create(), false)
            if (testModule != null)
                addImport(testModule!!, true)
        }

        stores = kodein.direct.instance<Set<Store<*>>>().toList()
        dispatcher = kodein.direct.instance()

        // Initialize Mini
        storeSubscriptions = MiniGen.subscribe(dispatcher, stores.toList())
        stores.forEach { store ->
            store.initialize()
        }

        dispatcher.addInterceptor(LoggerInterceptor(stores, { tag, msg ->
            Timber.tag(tag).d(msg)
        }))
    }

    /**
     * Sets a test module to use in UI tests. Must be followed by [initializeInjection].
     */
    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setTestModule(init: Kodein.Builder.() -> Unit) {
        testModule = Kodein.Module(name = "test", allowSilentOverride = true, init = init)
    }

    /**
     * Clears current test module. Must be followed by [initializeInjection].
     */
    @RestrictTo(RestrictTo.Scope.TESTS)
    fun clearTestModule() {
        testModule = null
    }
}
