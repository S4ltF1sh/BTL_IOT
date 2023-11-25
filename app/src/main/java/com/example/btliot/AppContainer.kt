package com.example.btliot

import com.example.btliot.view.home.HomeRepository
import com.example.btliot.view.home.chart.ChartRepository
import com.example.btliot.view.login.LoginRepository
import com.example.btliot.view.setting.SettingRepository

class AppContainer {
    var longinRepository: LoginRepository? = null
    var homeRepository: HomeRepository? = null
    var settingRepository: SettingRepository? = null
    var chartRepository: ChartRepository? = null

    init {
        longinRepository = LoginRepository()
        homeRepository = HomeRepository()
        settingRepository = SettingRepository()
        chartRepository = ChartRepository()
    }

    companion object {
        private var INSTANCE: AppContainer? = null

        fun getInstance(): AppContainer {
            if (INSTANCE == null) {
                INSTANCE = AppContainer()
            }
            return INSTANCE!!
        }
    }

    fun clear() {
        longinRepository = null
        homeRepository = null
        settingRepository = null
        chartRepository = null
    }
}