package com.example.btliot.view.alert

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.example.btliot.base.BaseFragment
import com.example.btliot.databinding.FragmentAlertBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AlertFragment : BaseFragment<FragmentAlertBinding>(FragmentAlertBinding::inflate) {
    private val viewModel by viewModels<AlertViewModel>(factoryProducer = { AlertViewModel.Factory })
    private var alertAdapter: AlertAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

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
            viewModel.allAlerts().collect {
                MainScope().launch {
                    alertAdapter?.submitList(it)
                    viewBinding.rvAlert.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.setAlertHandler(this@AlertFragment)
    }

    override val backPressCallback: () -> Boolean
        get() = {
            false
        }

    private fun setupRecyclerView() {
        alertAdapter = AlertAdapter { alertData ->
            //val gmmIntentUri = Uri.parse(alertData.userLocation)
//            val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
//                setPackage("com.google.android.apps.maps")
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(mapIntent)
        }
        viewBinding.rvAlert.adapter = alertAdapter
    }
}