package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.utils.Task
import mini.BaseAction
import mini.Reducer
import mini.Store

data class RequestAuthCompletedAction(
    val auth: LoginApi.Auth?,
    val task: Task
) : BaseAction()

class RequestAuthAction : BaseAction()

data class SessionState(
    val token: LoginApi.Auth? = null,
    val tokenTask: Task = Task.Idle()
)

class SessionStore(private val sessionController: SessionController) : Store<SessionState>() {

    init {
        newState = state.copy(token = sessionController.retrieveAuth())
    }

    @Reducer
    fun requestAuth(action: RequestAuthAction) {
        state.tokenTask.cancelJob()
        newState = state.copy(tokenTask = Task.Loading(sessionController.doAuth()))
    }

    @Reducer
    fun requestAuthCompleted(action: RequestAuthCompletedAction) {
        if (action.auth != null) sessionController.saveAuth(action.auth)

        newState = state.copy(token = action.auth, tokenTask = action.task)
    }
}
