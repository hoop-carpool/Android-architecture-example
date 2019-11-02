package com.hoopcarpool.archexample.features.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoopcarpool.archexample.core.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginController: LoginController) : BaseViewModel() {

    private val _viewData = MutableLiveData<LoginViewData>()
    val viewData: LiveData<LoginViewData>
        get() = _viewData

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun postValue(viewData: LoginViewData) {
        _viewData.postValue(viewData)
    }

    fun doLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            loginController.doLogin()
        }
    }

    data class LoginViewData(val text: String)
}
