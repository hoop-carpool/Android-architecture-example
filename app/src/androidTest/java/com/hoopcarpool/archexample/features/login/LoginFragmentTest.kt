package com.hoopcarpool.archexample.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.progress.KProgressBar
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.hoopcarpool.archexample.R
import com.hoopcarpool.archexample.core.extensions.bindViewModel
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.utils.injectTestDependencies
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import mini.onUi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.provider

private class LoginScreen : Screen<LoginScreen>() {
    val text = KTextView { withId(R.id.text) }
    val loading = KProgressBar { withId(R.id.loading) }
}

@RunWith(AndroidJUnit4::class)
internal class LoginFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockNavController: NavController = mock()
    private val mutableLiveData = MutableLiveData<Resource<LoginViewModel.LoginViewData>>()

    private lateinit var fragmentScenario: FragmentScenario<LoginFragment>

    private fun launchNewInstance() {
        injectTestDependencies {
            bindViewModel<LoginViewModel>() with provider {
                val viewModel = mock<LoginViewModel>()
                whenever(viewModel.getViewData()).thenReturn(mutableLiveData)
                viewModel
            }
        }
        fragmentScenario = launchFragmentInContainer(null, R.style.AppTheme) { LoginFragment() }
        fragmentScenario.onFragment { fragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController) }
    }

    @Before
    fun setup() {
        launchNewInstance()
    }

    @Test
    fun fragment_show_success_token() {

        val viewData = LoginViewModel.LoginViewData("token token")

        onUi { mutableLiveData.postValue(Resource.Success(viewData)) }

        Screen.onScreen<LoginScreen> {
            text.hasText(viewData.text)
            loading.isNotDisplayed()
        }
    }

    @Test
    fun fragment_show_loading() {
        onUi { mutableLiveData.postValue(Resource.Loading()) }

        Screen.onScreen<LoginScreen> {
            loading.isDisplayed()
        }
    }

    @Test
    fun fragment_click_calls() {

        Screen.onScreen<LoginScreen> {
            text.click()
            fragmentScenario.onFragment {
                verify(it.viewModel, times(1)).doLogin()
            }
        }
    }
}
