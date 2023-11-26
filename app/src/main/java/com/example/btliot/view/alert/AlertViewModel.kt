package com.example.btliot.view.alert

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.btliot.MyApp
import com.example.btliot.database.AlertDao
import com.example.btliot.model.AlertData
import com.example.btliot.view.home.chart.ChartRepository
import com.example.btliot.view.setting.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(
    private val mAlertDao: AlertDao,
    private val chartRepository: ChartRepository?,
    private val settingRepository: SettingRepository?
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AlertViewModel(
                        MyApp.getInstance().database.alertDao(),
                        MyApp.getInstance().appContainer.chartRepository,
                        MyApp.getInstance().appContainer.settingRepository
                    ) as T
                }
            }
    }

    fun allAlerts() = mAlertDao.getAll()

    fun insert(alert: AlertData) = mAlertDao.insert(alert)

    fun update(alert: AlertData) = mAlertDao.update(alert)

    fun delete(alert: AlertData) = mAlertDao.delete(alert)

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            chartRepository?.start()
            settingRepository?.start()
        }
    }

    fun setAlertHandler(
        lifecycleOwner: LifecycleOwner
    ) {
        chartRepository?.isFire?.observe(lifecycleOwner) { isFire ->
            if (isFire) {
                viewModelScope.launch(Dispatchers.IO) {
                    insert(
                        AlertData(
                            id = null,
                            userLocation = settingRepository?.userLocation?.value ?: "Unknown",
                            isFire = true
                        )
                    )
                }
            }
        }

        chartRepository?.isGasLeak?.observe(lifecycleOwner) { isGasLeak ->
            if (isGasLeak) {
                viewModelScope.launch(Dispatchers.IO) {
                    insert(
                        AlertData(
                            id = null,
                            userLocation = settingRepository?.userLocation?.value ?: "Unknown",
                            isFire = false
                        )
                    )
                }
            }
        }
    }
}