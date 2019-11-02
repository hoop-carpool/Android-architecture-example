package com.hoopcarpool.archexample.core.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> {
        return lazy { ViewModelProviders.of(this, direct.instance()).get(VM::class.java) }
    }
}
