package com.hoopcarpool.archexample.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.text.setOnClickListener {
            viewModel.doLogin()
        }

        viewModel.viewData.observe(this) {
            when {
                it.isFailure -> {
                    binding.loading.visibility = View.GONE
                }
                it.isSuccess -> {
                    binding.text.text = it.getOrNull()?.text
                    binding.loading.visibility = View.GONE
                }
                it.isLoading -> {
                    binding.loading.visibility = View.VISIBLE
                }
                it.isEmpty -> {
                    binding.loading.visibility = View.GONE
                }
            }
        }
    }
}
