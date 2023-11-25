package com.example.btliot.view.setting

import androidx.fragment.app.viewModels
import com.example.btliot.databinding.FragmentSettingBinding
import com.example.btliot.base.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private val viewModel by viewModels<SettingViewModel>(factoryProducer = { SettingViewModel.Factory })

    override fun initView() {
        setupToolbarVisible(false)
        super.initView()

        viewModel.setDefaultSettings(viewBinding)
    }

    override fun initAction() {
        super.initAction()

        viewModel.setBackButton(viewBinding) {
            backPressCallback()
        }

        viewModel.setUpdateSettingButton(viewBinding)
    }

    override fun initData() {
        super.initData()
    }
}