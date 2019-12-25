package com.hoopcarpool.archexample.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hoopcarpool.archexample.core.network.session.AuthApi
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private val loginUseCases: LoginUseCases = mock()
    private val observer: Observer<Resource<LoginViewModel.LoginViewData>> = mock()
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        reset(loginUseCases)
        loginViewModel = LoginViewModel(loginUseCases)
        coroutineRule.runBlockingTest {
            whenever(loginUseCases.doLogin(any())).doAnswer { Resource.Empty() }
        }
        loginViewModel.getViewData().observeForever(observer)
        verify(observer).onChanged(Resource.Empty())
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        loginViewModel.getViewData().removeObserver(observer)
    }

    @Test
    fun `dologin calls to Login use case`() {
        coroutineRule.runBlockingTest {
            loginViewModel.doLogin()
            verify(loginUseCases, times(1)).doLogin(any())
        }
    }

    @Test
    fun `do login never called without interaction`() {
        coroutineRule.runBlockingTest {
            verify(loginUseCases, never()).doLogin(any())
        }
    }

    @Test
    fun `after calling doLogin view data is Loading`() {
        loginViewModel.doLogin()
        verify(observer).onChanged(Resource.Loading())
    }

    @Test
    fun `view data is success on a success login call`() {
        coroutineRule.runBlockingTest {
            val auth = AuthApi.Auth("accessToken", "scope", "type")
            whenever(loginUseCases.doLogin(any())).doAnswer { Resource.Success(auth) }

            loginViewModel.doLogin()

            verify(observer).onChanged(Resource.Loading())
            verify(observer).onChanged(Resource.Success(LoginViewModel.LoginViewData(auth.accessToken)))
        }
    }

    @Test
    fun `view data is failure on a failure login call`() {
        coroutineRule.runBlockingTest {
            val exception = Exception("msg")
            whenever(loginUseCases.doLogin(any())).doAnswer { Resource.Failure(exception) }

            loginViewModel.doLogin()

            verify(observer).onChanged(Resource.Loading())
            verify(observer).onChanged(Resource.Failure(exception))
        }
    }
}
