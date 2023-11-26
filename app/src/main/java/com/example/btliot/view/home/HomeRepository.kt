package com.example.btliot.view.home

import com.example.btliot.common.CommonSharePreference
import com.example.btliot.common.UserType

class HomeRepository {
    private val sharePreference = CommonSharePreference.getInstance()

    fun logout() {
        sharePreference.isLogin = false
        sharePreference.rememberMe = false
    }

    fun getUserType(): UserType {
        return if (sharePreference.userName == "admin") {
            UserType.ADMIN
        } else {
            UserType.USER
        }
    }
}