package com.example.btliot.view.home.chart

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.btliot.databinding.FragmentChartBinding
import com.example.btliot.base.BaseFragment
import kotlinx.coroutines.launch

class ChartFragment : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {
    private val viewModel by viewModels<ChartViewModel>(factoryProducer = { ChartViewModel.Factory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun initView() {
        setupToolbarVisible(false)
        super.initView()
    }

    override fun initAction() {
        super.initAction()
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeData(viewBinding, this@ChartFragment)
            }
        }
    }

    override val backPressCallback: () -> Boolean
        get() = {
            true
        }
}