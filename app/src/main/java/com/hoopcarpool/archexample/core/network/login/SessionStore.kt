package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.network.Resource
import com.hoopcarpool.archexample.core.network.Task
import kotlinx.coroutines.Job
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
    val tokenTask: Task = Task.idle()
)

class SessionStore(private val sessionController: SessionController) : Store<SessionState>() {

    init {
        newState = state.copy(token = sessionController.retrieveAuth())
    }

    @Reducer
    fun requestAuth(action: RequestAuthAction) {
        if (state.tokenTask.isLoading) (state.tokenTask.value as Resource.Loading<Job>).value!!.cancel()
        newState = state.copy(tokenTask = Task.loading(sessionController.doAuth()))
    }

    @Reducer
    fun requestAuthCompleted(action: RequestAuthCompletedAction) {
        if (action.auth != null) sessionController.saveAuth(action.auth)

        newState = state.copy(token = action.auth, tokenTask = action.task)
    }
}
