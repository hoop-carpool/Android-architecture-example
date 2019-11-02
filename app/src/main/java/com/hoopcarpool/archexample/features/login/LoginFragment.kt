package com.hoopcarpool.archexample.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hoopcarpool.archexample.core.base.BaseFragment
import com.hoopcarpool.archexample.databinding.LoginFragmentBinding

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        LoginFragmentBinding
            .inflate(inflater, container, false)
            .also { binding = it }.root
    
}
