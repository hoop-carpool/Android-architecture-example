package com.hoopcarpool.archexample.features.login

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*

internal class LoginViewModelTest {

    @Test
    fun do_login_call_controller_method() {

        val loginController = mock(LoginController::class.java)
        val loginViewModel = LoginViewModel(loginController)

        loginViewModel.doLogin()

        runBlocking {
            verify(loginController, times(1)).doLogin()
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginController = mock(LoginController::class.java)
        val loginViewModel = LoginViewModel(loginController)

        runBlocking {
            verify(loginController, never()).doLogin()
        }
    }
}
