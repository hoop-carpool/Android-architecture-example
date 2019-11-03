package com.hoopcarpool.archexample.core.flux

import com.hoopcarpool.archexample.core.network.login.LoginApi
import mini.BaseAction
import mini.Reducer
import mini.Store

data class RequestAuthCompletedAction(
    val auth: LoginApi.Auth?
) : BaseAction()

data class SessionState(
    val token: LoginApi.Auth? = null
)

class SessionStore(private val sessionController: SessionController) : Store<SessionState>() {

    init {
        newState = state.copy(token = sessionController.retrieveAuth())
    }

    @Reducer
    fun requestAuthCompleted(action: RequestAuthCompletedAction) {
        if (action.auth != null) sessionController.saveAuth(action.auth)

        newState = state.copy(token = action.auth)
    }
}
