package com.hoopcarpool.archexample.utils

import com.hoopcarpool.archexample.app.app
import mini.onUiSync
import org.kodein.di.Kodein

/**
 * It overrides a [Kodein.Builder] and recreates the Kodein graph.
 */
fun injectTestDependencies(init: Kodein.Builder.() -> Unit) {
    app.setTestModule(init)

    onUiSync { app.initializeInjection() }
}
