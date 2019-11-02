package com.hoopcarpool.archexample.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.hoopcarpool.archexample.core.base.BaseFragment
import com.hoopcarpool.archexample.databinding.LoginFragmentBinding

class LoginFragment : BaseFragment() {

    val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        LoginFragmentBinding
            .inflate(inflater, container, false)
            .also { binding = it }.root

    override fun onResume() {
        super.onResume()

        viewModel.viewData.observe(this) {
            binding.text.text = it.text
        }
    }
}
