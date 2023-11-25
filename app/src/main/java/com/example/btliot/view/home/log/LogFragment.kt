package com.example.btliot.view.home.log

import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.example.btliot.databinding.FragmentLogBinding
import com.example.btliot.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LogFragment : BaseFragment<FragmentLogBinding>(FragmentLogBinding::inflate) {
    private val viewModel by viewModels<LogViewModel>(factoryProducer = { LogViewModel.Factory })
    private var logAdapter: LogAdapter? = null

    override fun initView() {
        setupToolbarVisible(false)
        super.initView()
        setupRecyclerView()
    }


    override fun initAction() {
        super.initAction()
    }

    override fun initData() {
        super.initData()

        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            viewModel.allLogs().collect {
                MainScope().launch {
                    logAdapter?.submitList(it)
                    viewBinding.rvLog.smoothScrollToPosition(0)
                }
            }
        }
    }

    override val backPressCallback: () -> Boolean
        get() = {
            false
        }

    private fun setupRecyclerView() {
        logAdapter = LogAdapter()
        viewBinding.rvLog.adapter = logAdapter
    }
}