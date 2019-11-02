package com.hoopcarpool.archexample.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.facebook.stetho.Stetho
import com.hoopcarpool.archexample.BuildConfig
import com.hoopcarpool.archexample.core.base.BaseViewModel
import com.hoopcarpool.archexample.features.login.LoginViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import mini.*
import mini.kodein.android.KodeinViewModelFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Builder
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
            addImport(ViewModelsModule.create(), false)
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

/**
 * Kodein module that provides app dependencies
 */
object AppModule {
    fun create() = Kodein.Module("app", true) {
        bind<Application>() with singleton { app }
        bind<Dispatcher>() with singleton { MiniGen.newDispatcher() }

        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein.direct) }

        bind() from setBinding<Store<*>>()
    }
}

/**
 * Kodein module that provide viewmodels
 */
object ViewModelsModule {
    fun create() = Kodein.Module("viewmodel", true) {
        bindViewModel<LoginViewModel>() with provider { LoginViewModel() }
    }

    private inline fun <reified T : BaseViewModel> Builder.bindViewModel(overrides: Boolean? = null): Builder.TypeBinder<T> {
        return bind<T>(T::class.java.simpleName, overrides)
    }
}

/**
 * Kodein module that provide network classes
 */
object NetworkModule {
    fun create() = Kodein.Module("network", true) {
        bind<OkHttpClient>() with singleton {
            val client = getOkHttpBuilder(60)
            client.build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .baseUrl(HttpUrl.parse("https://bhagavadgita.io/api/v1")!!)
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
