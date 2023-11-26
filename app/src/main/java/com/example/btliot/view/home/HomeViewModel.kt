package com.example.btliot.view.home

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.btliot.MyApp
import com.example.btliot.R
import com.example.btliot.common.UserType
import com.example.btliot.databinding.FragmentHomeBinding
import com.example.btliot.view.alert.AlertFragment
import com.example.btliot.view.home.chart.ChartFragment
import com.example.btliot.view.home.log.LogFragment

class HomeViewModel(private val homeRepository: HomeRepository?) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(MyApp.getInstance().appContainer.homeRepository) as T
                }
            }
    }

    fun logout() {
        homeRepository?.logout()
    }

    fun setupViewPager(viewBinding: FragmentHomeBinding, fragment: HomeFragment) {
        viewBinding.apply {
            viewPager.adapter =
                ViewPagerAdapter(
                    fragment,
                    if (homeRepository?.getUserType() == UserType.ADMIN)
                        listOf(AlertFragment())
                    else listOf(
                        ChartFragment(),
                        LogFragment()
                    )
                )

            viewPager.isUserInputEnabled = true

            if (homeRepository?.getUserType() != UserType.ADMIN)
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        bottomNav.menu.getItem(position).isChecked = true
                    }
                })
        }
    }

    fun setupBotNav(viewBinding: FragmentHomeBinding) {
        if (homeRepository?.getUserType() == UserType.ADMIN) {
            viewBinding.bottomNav.visibility = View.GONE

            return
        }

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

    fun setupDrawer(viewBinding: FragmentHomeBinding) {
        viewBinding.apply {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}