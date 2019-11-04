package com.hoopcarpool.archexample.features.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoopcarpool.archexample.core.base.BaseViewModel
import com.hoopcarpool.archexample.core.flux.SessionStore
import com.hoopcarpool.archexample.core.network.login.LoginApi
import com.hoopcarpool.archexample.core.network.login.LoginRepository
import com.hoopcarpool.archexample.core.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val sessionStore: SessionStore
) : BaseViewModel() {

    private val _viewData = MutableLiveData<Resource<LoginViewData>>()
    val viewData: LiveData<Resource<LoginViewData>>
        get() = _viewData

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun postValue(viewData: Resource<LoginViewData>) {
        _viewData.postValue(viewData)
    }

    init {
        _viewData.postValue(Resource.Empty())
//        sessionStore.flowable()
//            .select { it.token }
//            .subscribe {
//                _viewData.postValue(Resource.Success(LoginViewData(it.accessToken)))
//            }.track()
    }

    fun doLogin(): Job {
        _viewData.postValue(Resource.Loading())
        return CoroutineScope(Dispatchers.IO).launch {
            val oauth = loginRepository.doLogin()
            _viewData.postValue(LoginViewData.from(oauth))
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
