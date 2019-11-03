package com.hoopcarpool.archexample.features.login

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.progress.KProgressBar
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.hoopcarpool.archexample.R
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.utils.injectTestDependencies
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

private class LoginScreen : Screen<LoginScreen>() {
    val text = KTextView { withId(R.id.text) }
    val loading = KProgressBar { withId(R.id.loading) }
}

@RunWith(AndroidJUnit4::class)
internal class LoginFragmentTest {

    private val mockNavController: NavController = mock()
    private lateinit var fragmentScenario: FragmentScenario<LoginFragment>

    private fun launchNewInstance() {
        injectTestDependencies {
            bind<LoginViewModel>() with singleton {
                mock<LoginViewModel>()
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

        fragmentScenario.onFragment {
            it.viewModel.postValue(Resource.Success(viewData))
        }

        Screen.onScreen<LoginScreen> {
            text.hasText(viewData.text)
            loading.isNotDisplayed()
        }
    }

    @Test
    fun fragment_show_loading() {

        fragmentScenario.onFragment {
            it.viewModel.postValue(Resource.Loading())
        }

        Screen.onScreen<LoginScreen> {
            loading.isDisplayed()
        }
    }

    @Test
    fun fragment_click_calls() {

        Screen.onScreen<LoginScreen> {
            loading.isNotDisplayed()
            text.click()
            loading.isDisplayed()
        }
    }
}
