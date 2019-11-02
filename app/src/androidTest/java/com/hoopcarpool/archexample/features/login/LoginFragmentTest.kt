package com.hoopcarpool.archexample.features.login

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.hoopcarpool.archexample.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

private class LoginScreen : Screen<LoginScreen>() {
    val name = KTextView { withId(R.id.text) }
}

@RunWith(AndroidJUnit4::class)
internal class LoginFragmentTest {

    private val mockNavController = Mockito.mock(NavController::class.java)
    private lateinit var fragmentScenario: FragmentScenario<LoginFragment>

    private fun launchNewInstance() {
        fragmentScenario = launchFragmentInContainer(null, R.style.AppTheme) { LoginFragment() }
        fragmentScenario.onFragment { fragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController) }
    }

    @Before
    fun setup() {
        launchNewInstance()
    }

    @Test
    fun test_test() {

        val viewData = LoginViewModel.LoginViewData("This is a test")

        fragmentScenario.onFragment {
            it.viewModel.postValue(viewData)
        }

        Screen.onScreen<LoginScreen> {
            name.hasText(viewData.text)
        }
    }
}
