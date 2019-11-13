package com.hoopcarpool.archexample.features.login

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.hoopcarpool.archexample.core.base.BaseFragment
import com.hoopcarpool.archexample.core.utils.Resource
import com.hoopcarpool.archexample.databinding.LoginFragmentBinding

class LoginFragment : BaseFragment() {

    val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoginFragmentBinding
        .inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.text.setOnClickListener {
            viewModel.doLogin()
        }

        viewModel.doLogin()

        Handler().postDelayed({
            viewModel.doLogin()
        }, 150)

//        Handler().postDelayed({
//            val intent = Intent(context, BlankActivity::class.java)
//            startActivity(intent)
//            activity?.finish()
//        }, 1000)

        viewModel.getViewData().observe(this) {
            it.logIt("LoginFragment")
            when (it) {
                is Resource.Success -> {
                    binding.text.text = it.value.text
                    binding.loading.visibility = View.GONE
                }
                is Resource.Empty -> {
                    binding.loading.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                }
                is Resource.Failure -> {
                    binding.loading.visibility = View.GONE
                    binding.text.text = it.exception.toString()
                }
            }
        }
    }
}
