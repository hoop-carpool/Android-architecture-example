package com.hoopcarpool.archexample.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hoopcarpool.archexample.core.flux.SessionController
import com.hoopcarpool.archexample.core.flux.SessionStore
import com.hoopcarpool.archexample.core.network.login.LoginApi
import com.hoopcarpool.archexample.core.network.login.LoginRepository
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

    private val loginRepository: LoginRepository = mock()
    private val sessionController: SessionController = mock()
    private val sessionStore = SessionStore(sessionController)

    @Before
    fun setup() {
        reset(loginRepository)
    }

    @Test
    fun init_has_empty_resource() {

        val loginViewModel = LoginViewModel(loginRepository, sessionStore)

        Assertions.assertTrue(loginViewModel.viewData.value is Resource.Empty)
    }

    @Test
    fun do_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginRepository, sessionStore)

        runBlocking {
            loginViewModel.doLogin()
            verify(loginRepository, times(1)).doLogin()
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginRepository, sessionStore)

        runBlocking {
            verify(loginRepository, never()).doLogin()
        }
    }

    @Test
    fun login_call_set_resource_loading() {

        val loginViewModel = LoginViewModel(loginRepository, sessionStore)

        loginViewModel.doLogin()
        Assertions.assertTrue(loginViewModel.viewData.value is Resource.Loading)
    }

    @Test
    fun login_call_return_success() {
        runBlocking {
            val loginViewModel = LoginViewModel(loginRepository, sessionStore)
            val auth = LoginApi.Auth("accessToken", "scope", "type")
            whenever(loginRepository.doLogin()).doAnswer { Resource.Success(auth) }

            loginViewModel.doLogin().join()
            Assertions.assertTrue(loginViewModel.viewData.value is Resource.Success)
            Assertions.assertTrue((loginViewModel.viewData.value as Resource.Success).value.text == auth.accessToken)
        }
    }
}
