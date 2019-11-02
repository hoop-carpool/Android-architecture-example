package com.hoopcarpool.archexample.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoopcarpool.archexample.core.base.BaseViewModel
import okhttp3.OkHttpClient
import timber.log.Timber

class LoginViewModel : BaseViewModel() {

    private val _viewData = MutableLiveData<LoginViewData>()
    val viewData: LiveData<LoginViewData>
        get() = _viewData

    class LoginViewData
}
