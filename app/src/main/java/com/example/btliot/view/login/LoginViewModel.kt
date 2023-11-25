package com.example.btliot.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.btliot.MyApp
import com.example.btliot.model.Account

class LoginViewModel(private val loginRepository: LoginRepository?) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(MyApp.getInstance().appContainer.longinRepository) as T
                }
            }
    }

    fun login(username: String, password: String, isRemember: Boolean): Boolean {
        return loginRepository?.login(username, password, isRemember) == true
    }

    fun getRememberedAccount(): Account? {
        return loginRepository?.getRememberedAccount()
    }
}