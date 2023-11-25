package com.example.btliot.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.btliot.MyApp

class HomeViewModel(private val homeRepository: HomeRepository?) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(MyApp.getInstance().appContainer.homeRepository) as T
                }
            }
    }

    fun logout() {
        homeRepository?.logout()
    }
}