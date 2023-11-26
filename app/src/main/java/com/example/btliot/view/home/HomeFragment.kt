package com.example.btliot.view.home

import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.btliot.R
import com.example.btliot.databinding.FragmentHomeBinding
import com.example.btliot.base.BaseFragment
import com.example.btliot.view.home.chart.ChartFragment
import com.example.btliot.view.home.log.LogFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { HomeViewModel.Factory })

    override fun initView() {
        super.initView()
        setupToolbarVisible(false)
        viewModel.apply {
            setupViewPager(viewBinding, this@HomeFragment)
            setupBotNav(viewBinding)
            setupDrawer(viewBinding)
        }

    }

    override fun initAction() {
        super.initAction()

        viewBinding.apply {
            toolbar.setNavigationOnClickListener {
                drawerLayout.open()
            }
        }

        viewBinding.drawerBody.apply {
            tvSetting.setOnClickListener {
                navigate(R.id.action_homeFragment_to_settingFragment)
            }

            tvLogout.setOnClickListener {
                viewModel.logout()
                navigate(R.id.action_homeFragment_to_loginFragment)
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
}