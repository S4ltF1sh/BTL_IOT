package com.example.btliot.common

import android.content.Context
import android.content.SharedPreferences

class CommonSharePreference {
    private var sharedPreferences: SharedPreferences? = null


    companion object {
        private const val SHARED_PREFERENCES_NAME = "btl_iot"
        private lateinit var instance: CommonSharePreference

        fun init(context: Context) {
            instance = CommonSharePreference()
            instance.sharedPreferences = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        @Synchronized
        fun getInstance(): CommonSharePreference {
            if (!this::instance.isInitialized) {
                instance = CommonSharePreference()
                return instance
            }
            return instance
        }
    }

    private fun getSharedPreferences() =
        instance.sharedPreferences

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        getSharedPreferences()?.getBoolean(key, defaultValue) ?: defaultValue

    fun putBoolean(key: String, value: Boolean) {
        val editor = getSharedPreferences()?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int =
        getSharedPreferences()?.getInt(key, defaultValue) ?: defaultValue

    fun putInt(key: String, value: Int) {
        val editor = getSharedPreferences()?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun getLong(key: String, defaultValue: Long): Long =
        getSharedPreferences()?.getLong(key, defaultValue) ?: defaultValue

    fun putLong(key: String, value: Long) {
        val editor = getSharedPreferences()?.edit()
        editor?.putLong(key, value)
        editor?.apply()
    }

    fun getString(key: String, defaultValue: String? = Strings.EMPTY): String =
        getSharedPreferences()?.getString(key, defaultValue) ?: Strings.EMPTY

    fun putString(key: String, value: String) {
        val editor = getSharedPreferences()?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    var rememberMe: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_REMEMBER, false) ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_REMEMBER, value)?.apply()
        }

    var isLogin: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_LOGIN, false) ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_LOGIN, value)?.apply()
        }

    var userName: String
        get() {
            return getSharedPreferences()?.getString(Key.KEY_LOGIN_USERNAME, Strings.EMPTY)
                ?: Strings.EMPTY
        }
        set(value) {
            getSharedPreferences()?.edit()?.putString(Key.KEY_LOGIN_USERNAME, value)?.apply()
        }

    var password: String
        get() {
            return getSharedPreferences()?.getString(Key.KEY_LOGIN_PASSWORD, Strings.EMPTY)
                ?: Strings.EMPTY
        }
        set(value) {
            getSharedPreferences()?.edit()?.putString(Key.KEY_LOGIN_PASSWORD, value)?.apply()
        }

    //setting:
    var userLocation: String
        get() {
            return getSharedPreferences()?.getString(Key.KEY_USER_LOCATION, Strings.EMPTY)
                ?: Strings.EMPTY
        }
        set(value) {
            getSharedPreferences()?.edit()?.putString(Key.KEY_USER_LOCATION, value)?.apply()
        }

    var userPhoneNumber: String
        get() {
            return getSharedPreferences()?.getString(Key.KEY_USER_PHONE_NUMBER, Strings.EMPTY)
                ?: Strings.EMPTY
        }
        set(value) {
            getSharedPreferences()?.edit()?.putString(Key.KEY_USER_PHONE_NUMBER, value)?.apply()
        }

    var fireFighterPhoneNumber: String
        get() {
            return getSharedPreferences()?.getString(
                Key.KEY_FIRE_FIGHTER_PHONE_NUMBER,
                Strings.EMPTY
            )
                ?: Strings.EMPTY
        }
        set(value) {
            getSharedPreferences()?.edit()?.putString(Key.KEY_FIRE_FIGHTER_PHONE_NUMBER, value)
                ?.apply()
        }

    var temperatureFireAlert: Float
        get() {
            return getSharedPreferences()?.getFloat(
                Key.KEY_SETTING_TEMPERATURE_FIRE_ALERT,
                50f
            )
                ?: 50f
        }
        set(value) {
            getSharedPreferences()?.edit()?.putFloat(Key.KEY_SETTING_TEMPERATURE_FIRE_ALERT, value)
                ?.apply()
        }

    var gasAlert: Float
        get() {
            return getSharedPreferences()?.getFloat(
                Key.KEY_SETTING_GAS_ALERT,
                50f
            )
                ?: 50f
        }
        set(value) {
            getSharedPreferences()?.edit()?.putFloat(Key.KEY_SETTING_GAS_ALERT, value)
                ?.apply()
        }

    var temperatureAutoAirConditioner: Float
        get() {
            return getSharedPreferences()?.getFloat(
                Key.KEY_SETTING_TEMPERATURE_AUTO_AIR_CONDITIONER,
                36f
            )
                ?: 36f
        }
        set(value) {
            getSharedPreferences()?.edit()
                ?.putFloat(Key.KEY_SETTING_TEMPERATURE_AUTO_AIR_CONDITIONER, value)
                ?.apply()
        }

    var settingAllowAutoAirConditioner: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(
                Key.KEY_SETTING_ALLOW_AUTO_AIR_CONDITIONER,
                false
            )
                ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(
                Key.KEY_SETTING_ALLOW_AUTO_AIR_CONDITIONER,
                value
            )?.apply()
        }

    var settingAllowFireAlert: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_SETTING_ALLOW_FIRE_ALERT, false)
                ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_SETTING_ALLOW_FIRE_ALERT, value)
                ?.apply()
        }

    var settingAllowPump: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_SETTING_ALLOW_PUMP, false) ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_SETTING_ALLOW_PUMP, value)?.apply()
        }

    var settingAllowNotification: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_SETTING_ALLOW_NOTIFICATION, false)
                ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_SETTING_ALLOW_NOTIFICATION, value)
                ?.apply()
        }

    var settingAllowNotiSound: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_SETTING_ALLOW_NOTI_SOUND, false)
                ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_SETTING_ALLOW_NOTI_SOUND, value)
                ?.apply()
        }

    var settingAllowNotiVibrate: Boolean
        get() {
            return getSharedPreferences()?.getBoolean(Key.KEY_SETTING_ALLOW_NOTI_VIBRATE, false)
                ?: false
        }
        set(value) {
            getSharedPreferences()?.edit()?.putBoolean(Key.KEY_SETTING_ALLOW_NOTI_VIBRATE, value)
                ?.apply()
        }
}