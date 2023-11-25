package com.example.btliot.view.login

import com.example.btliot.common.CommonSharePreference
import com.example.btliot.model.Account


class LoginRepository {
    private val sharePreference = CommonSharePreference.getInstance()
    private val adminAccount = Account("admin", "admin")
    private val userAccount = Account("user", "user")

    fun login(username: String, password: String, isRemember: Boolean): Boolean {
        return if (username == adminAccount.username && password == adminAccount.password) {
            if (isRemember) {
                sharePreference.userName = username
                sharePreference.password = password
            }

            sharePreference.rememberMe = isRemember
            sharePreference.isLogin = true

            true
        } else if (username == userAccount.username && password == userAccount.password) {
            if (isRemember) {
                sharePreference.userName = username
                sharePreference.password = password
            }

            sharePreference.rememberMe = isRemember
            sharePreference.isLogin = true

            true
        } else {
            false
        }
    }

    fun getRememberedAccount(): Account? {
        val username = sharePreference.userName
        val password = sharePreference.password

        return if (sharePreference.rememberMe && username.isNotEmpty() && password.isNotEmpty()) {
            Account(sharePreference.userName, sharePreference.password)
        } else {
            null
        }
    }
}