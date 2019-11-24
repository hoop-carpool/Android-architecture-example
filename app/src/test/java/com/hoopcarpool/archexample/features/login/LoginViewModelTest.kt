package com.hoopcarpool.archexample.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hoopcarpool.archexample.core.network.login.LoginApi
import com.hoopcarpool.archexample.core.network.login.LoginUseCases
import com.hoopcarpool.archexample.core.utils.Resource
import com.nhaarman.mockitokotlin2.*
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

internal class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val loginUseCases: LoginUseCases = mock()

    @Before
    fun setup() {
        reset(loginUseCases)
    }

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun init_has_empty_resource() {

        val loginViewModel = LoginViewModel(loginUseCases)

        Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Empty)
    }

    @Test
    fun do_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginUseCases)

        runBlocking {
            loginViewModel.doLogin()
            verify(loginUseCases, times(1)).doLogin()
        }
    }

    @Test
    fun dont_login_call_controller_method() {

        val loginViewModel = LoginViewModel(loginUseCases)

        runBlocking {
            verify(loginUseCases, never()).doLogin()
        }
    }

    @Test
    fun login_call_set_resource_loading() {

        val loginViewModel = LoginViewModel(loginUseCases)

        loginViewModel.doLogin()
        Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Loading)
    }

    @Test
    fun login_call_return_success() {
        runBlocking {
            val loginViewModel = LoginViewModel(loginUseCases)
            val auth = LoginApi.Auth("accessToken", "scope", "type")
            whenever(loginUseCases.doLogin()).doAnswer { Resource.Success(auth) }

            loginViewModel.doLogin().join()
            Assertions.assertTrue(loginViewModel.getViewData().value is Resource.Success)
            Assertions.assertTrue((loginViewModel.getViewData().value as Resource.Success).value.text == auth.accessToken)
        }
    }
}
