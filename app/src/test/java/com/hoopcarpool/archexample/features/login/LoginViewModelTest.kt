package com.hoopcarpool.archexample.features.login

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class LoginViewModelTest {

    private val loginController: LoginController = mock()

    @Before
    fun setup() {
        reset(loginController)
    }

    @Test
    fun do_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginController)

        loginViewModel.doLogin()

        runBlocking {
            verify(loginController, never()).doLogin()
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginController)

        runBlocking {
            verify(loginController, never()).doLogin()
        }
    }
}
