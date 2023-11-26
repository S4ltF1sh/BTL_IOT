package com.example.btliot

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.btliot.common.UserType
import com.example.btliot.notification.NotiManager
import com.example.btliot.view.home.chart.ChartRepository
import com.example.btliot.view.setting.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val chartRepository: ChartRepository?,
    private val settingRepository: SettingRepository?
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainActivityViewModel(
                        MyApp.getInstance().appContainer.chartRepository,
                        MyApp.getInstance().appContainer.settingRepository
                    ) as T
                }
            }
    }

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            chartRepository?.start()
            settingRepository?.start()
        }

    }

    fun setNotificationHandler(
        lifecycleOwner: LifecycleOwner,
        notiHandler: NotiManager.NotiHandler
    ) {
        chartRepository?.isFire?.observe(lifecycleOwner) { isFire ->
            if (isFire) {
                val isGasLeak = chartRepository.isGasLeak.value == true

                if (settingRepository?.getUserType() == UserType.ADMIN) {
                    notiHandler.onAdminNoti(
                        settingRepository?.allowNotification?.value == true,
                        settingRepository?.userLocation?.value ?: "Unknown",
                        settingRepository?.allowNotiSound?.value == true,
                        settingRepository?.allowNotiVibrate?.value == true
                    )
                } else {
                    notiHandler.onUserNoti(
                        settingRepository?.allowNotification?.value == true,
                        chartRepository.getStatusString(isFire, isGasLeak),
                        settingRepository?.allowNotiSound?.value == true,
                        settingRepository?.allowNotiVibrate?.value == true
                    )
                }
            }
        }

        chartRepository?.isGasLeak?.observe(lifecycleOwner) { isGasLeak ->
            if (isGasLeak) {
                val isFire = chartRepository.isFire.value == true

                if (settingRepository?.getUserType() == UserType.ADMIN) {
                    notiHandler.onAdminNoti(
                        settingRepository?.allowNotification?.value == true,
                        settingRepository?.userLocation?.value ?: "Unknown",
                        settingRepository?.allowNotiSound?.value == true,
                        settingRepository?.allowNotiVibrate?.value == true
                    )
                } else {
                    notiHandler.onUserNoti(
                        settingRepository?.allowNotification?.value == true,
                        chartRepository.getStatusString(isFire, isGasLeak),
                        settingRepository?.allowNotiSound?.value == true,
                        settingRepository?.allowNotiVibrate?.value == true
                    )
                }
            }
        }
    }

    fun setUserLocation(newLocation: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository?.setUserLocation(newLocation)
        }
    }
}