package com.hoopcarpool.archexample.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoopcarpool.archexample.core.base.BaseViewModel
import com.hoopcarpool.archexample.core.network.login.LoginApi
import com.hoopcarpool.archexample.core.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

open class LoginViewModel(private val loginUseCases: LoginUseCases) : BaseViewModel() {

    private val _viewData = MutableLiveData<Resource<LoginViewData>>()
    open fun getViewData(): LiveData<Resource<LoginViewData>> = _viewData

    init {
        _viewData.postValue(Resource.Empty())
    }

    open fun doLogin(): Job {
        viewModelScope.coroutineContext.cancelChildren()
        _viewData.postValue(Resource.Loading())
        return viewModelScope.launch {
            val oauth = loginUseCases.doLogin()
            _viewData.postValue(LoginViewData.from(oauth))
        }
    }

    fun blabla(): Boolean {
        viewModelScope.launch {
            // lanzar accion

            // se subscribe a la task/store -> se suspende aqui

            // aqui tenemos ya la task success o failure
        }
    }

    data class LoginViewData(val text: String) {

        companion object {
            fun from(oauthResource: Resource<LoginApi.Auth>): Resource<LoginViewData> {
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
