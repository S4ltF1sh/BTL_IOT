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
        setupViewPager()
        setupBotNav()
        setupDrawer()
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

    private fun setupViewPager() {
        viewBinding.apply {
            viewPager.adapter =
                ViewPagerAdapter(this@HomeFragment, listOf(ChartFragment(), LogFragment()))
            viewPager.isUserInputEnabled = true
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    bottomNav.menu.getItem(position).isChecked = true
                }
            })
        }
    }

    private fun setupBotNav() {
        viewBinding.apply {
            bottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.chartFragment -> {
                        viewPager.setCurrentItem(0, true)
                        true
                    }

                    else -> {
                        viewPager.setCurrentItem(1, true)
                        true
                    }
                }
            }
        }
    }

    private fun setupDrawer() {
        viewBinding.apply {
            drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    override val backPressCallback: () -> Boolean
        get() = {
            activity?.finish()
            true
        }
}