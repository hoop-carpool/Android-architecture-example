package com.hoopcarpool.archexample.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.hoopcarpool.archexample.BuildConfig
import java.io.Closeable
import kotlin.properties.Delegates
import mini.*
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.*
import timber.log.Timber

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

class App : Application(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)

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
    private fun initializeInjection() {
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
            addImport(ViewModelModule.create(), false)
            addImport(NetworkModule.create(), false)
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
}

class FakeAction : BaseAction()
class FakeStore : Store<String>() {
    @Reducer
    fun fakeAction(action: FakeAction) {
        newState = ""
    }
}
