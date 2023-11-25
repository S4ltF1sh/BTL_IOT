package com.example.btliot.view.home

import com.example.btliot.common.CommonSharePreference

class HomeRepository {
    private val sharePreference = CommonSharePreference.getInstance()

    fun logout() {
        sharePreference.isLogin = false
        sharePreference.rememberMe = false
    }
}