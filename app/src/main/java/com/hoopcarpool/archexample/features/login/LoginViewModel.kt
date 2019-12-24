package com.hoopcarpool.archexample.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoopcarpool.archexample.core.base.BaseViewModel
import com.hoopcarpool.archexample.core.network.login.AuthApi
import com.hoopcarpool.archexample.core.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCases: LoginUseCases) : BaseViewModel() {

    private val _viewData = MutableLiveData<Resource<LoginViewData>>()

    fun getViewData(): LiveData<Resource<LoginViewData>> = _viewData

    init {
        _viewData.postValue(Resource.Empty())
    }

    fun doLogin(): Job {
        _viewData.postValue(Resource.Loading())
        return viewModelScope.launch {
            val oauth = loginUseCases.doLogin(this@LoginViewModel)
            _viewData.postValue(LoginViewData.from(oauth))
        }
    }

    data class LoginViewData(val text: String) {

        companion object {
            fun from(oauthResource: Resource<AuthApi.Auth>): Resource<LoginViewData> {
                return when (oauthResource) {
                    is Resource.Success -> Resource.Success(LoginViewData(oauthResource.value.accessToken))
                    is Resource.Empty -> Resource.Empty()
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Failure -> Resource.Failure(oauthResource.exception)
                }
            }
        }
    }
}
