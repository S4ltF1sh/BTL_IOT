package com.example.btliot.view.login

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.btliot.R
import com.example.btliot.databinding.FragmentLoginBinding
import com.example.btliot.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>(factoryProducer = { LoginViewModel.Factory })

    override fun initView() {
        setupToolbarVisible(false)
        setStatusBarColor(R.color.color_red_login)

        super.initView()

        viewBinding.apply {
            viewModel.getRememberedAccount()?.let {
                edtUsername.setText(it.username)
                edtPassword.setText(it.password)
                imgRemember.isSelected = true

                onLoginHandler(it.username, it.password, true)
            }
        }

    }

    override fun initAction() {
        super.initAction()

        viewBinding.apply {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                val isRemember = imgRemember.isSelected

                onLoginHandler(username, password, isRemember)
            }

            imgRemember.setOnClickListener {
                imgRemember.isSelected = !imgRemember.isSelected
            }
        }
    }

    override fun initData() {
        super.initData()
    }

    override val backPressCallback: () -> Boolean
        get() = {
            activity?.finish()
            true
        }

    private fun onLoginHandler(username: String, password: String, isRemember: Boolean) {
        if (viewModel.login(username, password, isRemember)) {
            navigate(R.id.action_loginFragment_to_homeFragment)
        } else {
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        setStatusBarColor(R.color.color_bg)
        super.onStop()
    }
}