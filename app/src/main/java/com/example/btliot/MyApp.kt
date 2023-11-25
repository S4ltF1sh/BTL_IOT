package com.example.btliot

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.btliot.common.CommonSharePreference
import com.example.btliot.database.AppDatabase
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    val appContainer: AppContainer by lazy {
        AppContainer()
    }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    companion object {
        private lateinit var instance: MyApp

        @JvmStatic
        @Synchronized
        fun getInstance(): MyApp {
            if (!this::instance.isInitialized) {
                instance = MyApp()
                return instance
            }
            return instance
        }

        fun context(): Context {
            return getInstance().applicationContext
        }
    }

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        instance = this
        CommonSharePreference.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}