package com.hoopcarpool.archexample.features.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoopcarpool.archexample.core.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mini.Resource

class LoginViewModel(private val loginController: LoginController) : BaseViewModel() {

    private val _viewData = MutableLiveData<Resource<LoginViewData>>()
    val viewData: LiveData<Resource<LoginViewData>>
        get() = _viewData

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun postValue(viewData: Resource<LoginViewData>) {
        _viewData.postValue(viewData)
    }

    init {
        _viewData.postValue(Resource.empty())
    }

    fun doLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            _viewData.postValue(Resource.loading())
            val oauth = loginController.doLogin()
            _viewData.postValue(LoginViewData.from(oauth))
        }
    }

    data class LoginViewData(val text: String) {

        companion object {
            fun from(oauthResource: Resource<Auth>): Resource<LoginViewData> {
                return when {
                    oauthResource.isLoading -> Resource.loading()
                    oauthResource.isSuccess -> Resource.success(LoginViewData(oauthResource.getOrNull()!!.accessToken))
                    oauthResource.isFailure -> Resource.failure(oauthResource.exceptionOrNull())
                    else -> Resource.empty()
                }
            }
        }
    }
}
