package com.hoopcarpool.archexample.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hoopcarpool.archexample.core.network.login.LoginApi
import com.hoopcarpool.archexample.core.network.login.SessionController
import com.hoopcarpool.archexample.core.network.login.SessionStore
import com.hoopcarpool.archexample.core.utils.Resource
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

internal class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val loginUseCases: LoginUseCases = mock()
    private val sessionController: SessionController = mock()
    private val sessionStore = SessionStore(sessionController)

    @Before
    fun setup() {
        reset(loginUseCases)
    }

    @Test
    fun init_has_empty_resource() {

        val loginViewModel = LoginViewModel(loginUseCases, sessionStore)

        Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Empty)
    }

    @Test
    fun do_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginUseCases, sessionStore)

        runBlocking {
            loginViewModel.doLogin()
            verify(loginUseCases, times(1)).doLogin(number)
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginUseCases, sessionStore)

        runBlocking {
            verify(loginUseCases, never()).doLogin(number)
        }
    }

    @Test
    fun login_call_set_resource_loading() {

        val loginViewModel = LoginViewModel(loginUseCases, sessionStore)

        loginViewModel.doLogin()
        Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Loading)
    }

    @Test
    fun login_call_return_success() {
        runBlocking {
            val loginViewModel = LoginViewModel(loginUseCases, sessionStore)
            val auth = LoginApi.Auth("accessToken", "scope", "type")
            whenever(loginUseCases.doLogin(number)).doAnswer { Resource.Success(auth) }

            loginViewModel.doLogin().join()
            Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Success)
            Assertions.assertTrue((loginViewModel.getViewData().value as Resource.Success).value.text == auth.accessToken)
        }
    }
}
