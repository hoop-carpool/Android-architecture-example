package com.hoopcarpool.archexample.core.extensions

import com.hoopcarpool.archexample.core.base.BaseViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

inline fun <reified T : BaseViewModel> Kodein.Builder.bindViewModel(overrides: Boolean? = null): Kodein.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}
