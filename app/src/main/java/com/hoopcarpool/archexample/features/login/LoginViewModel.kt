package com.hoopcarpool.archexample.features.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoopcarpool.archexample.core.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    private val _viewData = MutableLiveData<LoginViewData>()
    val viewData: LiveData<LoginViewData>
        get() = _viewData

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun postValue(viewData: LoginViewData) {
        _viewData.postValue(viewData)
    }

    data class LoginViewData(val text: String)
}
