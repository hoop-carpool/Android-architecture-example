package com.hoopcarpool.archexample.features.login

import com.hoopcarpool.archexample.core.network.login.LoginRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class LoginViewModelTest {

    private val loginRepository: LoginRepository = mock()

    @Before
    fun setup() {
        reset(loginRepository)
    }

    @Test
    fun do_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginRepository)

        loginViewModel.doLogin()

        runBlocking {
            verify(loginRepository, never()).doLogin()
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginRepository)

        runBlocking {
            verify(loginRepository, never()).doLogin()
        }
    }
}
