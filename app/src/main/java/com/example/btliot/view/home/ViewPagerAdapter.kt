package com.example.btliot.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.btliot.base.BaseFragment

class ViewPagerAdapter(parentFm: Fragment, private val listFm: List<BaseFragment<*>>) :
    FragmentStateAdapter(parentFm) {
    override fun getItemCount(): Int = listFm.size

    override fun createFragment(position: Int): Fragment {
        return listFm[position]
    }
}