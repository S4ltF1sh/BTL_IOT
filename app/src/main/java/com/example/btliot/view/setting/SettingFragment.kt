package com.example.btliot.view.setting

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.btliot.databinding.FragmentSettingBinding
import com.example.btliot.base.BaseFragment
import kotlinx.coroutines.launch

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private val viewModel by viewModels<SettingViewModel>(factoryProducer = { SettingViewModel.Factory })

    override fun initView() {
        setupToolbarVisible(false)
        super.initView()

        viewModel.setOnUserChangeSetting(viewBinding)
        viewModel.start()
    }

    override fun initAction() {
        super.initAction()

        viewModel.setBackButton(viewBinding) {
            backPressCallback()
        }
        viewModel.setInformationActions(viewBinding)
        viewModel.setUpdateSettingButton(viewBinding)
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeData(viewBinding, this@SettingFragment)
            }
        }
    }
}