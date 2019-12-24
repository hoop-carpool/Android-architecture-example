package com.hoopcarpool.archexample.core.network.login

import com.hoopcarpool.archexample.core.utils.JobTask
import mini.BaseAction
import mini.Reducer
import mini.Store

data class RequestAuthCompletedAction(
    val auth: AuthApi.Auth?,
    val jobTask: JobTask
) : BaseAction()

class RequestAuthAction : BaseAction()

data class SessionState(
    val token: AuthApi.Auth? = null,
    val tokenJobTask: JobTask = JobTask.Idle()
)

class SessionStore(private val sessionController: SessionController) : Store<SessionState>() {

    init {
        newState = state.copy(token = sessionController.retrieveAuth())
    }

    @Reducer
    fun requestAuth(action: RequestAuthAction) {
        state.tokenJobTask.cancelJob("Retry auth")
        newState = state.copy(tokenJobTask = JobTask.Loading(sessionController.doAuth()))
    }

    @Reducer
    fun requestAuthCompleted(action: RequestAuthCompletedAction) {
        if (action.auth != null) sessionController.saveAuth(action.auth)

        newState = state.copy(token = action.auth, tokenJobTask = action.jobTask)
    }
}
