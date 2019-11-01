package com.hoopcarpool.hooparchexample.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hoopcarpool.hooparchexample.core.base.BaseFragment
import com.hoopcarpool.hooparchexample.databinding.LoginFragmentBinding
import mini.kodein.android.viewModel

class LoginFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoginFragmentBinding.inflate(inflater, container, false).root
}
