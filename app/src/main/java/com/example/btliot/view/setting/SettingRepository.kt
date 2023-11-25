package com.example.btliot.view.setting

import com.example.btliot.common.CommonSharePreference

class SettingRepository {
    private val sharePreference = CommonSharePreference.getInstance()

    fun updateSettings(
        newUserLocation: String,
        newUserPhoneNumber: String,
        newFireFighterPhoneNumber: String,
        fireAlert: Boolean,
        pump: Boolean,
        notification: Boolean,
        notiSound: Boolean,
        notiVibrate: Boolean
    ) {
        sharePreference.userLocation = newUserLocation
        sharePreference.userPhoneNumber = newUserPhoneNumber
        sharePreference.fireFighterPhoneNumber = newFireFighterPhoneNumber
        sharePreference.settingAllowFireAlert = fireAlert
        sharePreference.settingAllowPump = pump
        sharePreference.settingAllowNotification = notification
        sharePreference.settingAllowNotiSound = notiSound
        sharePreference.settingAllowNotiVibrate = notiVibrate
    }

    val userLocation: String
        get() = sharePreference.userLocation

    val userPhoneNumber: String
        get() = sharePreference.userPhoneNumber

    val fireFighterPhoneNumber: String
        get() = sharePreference.fireFighterPhoneNumber

    val settingAllowFireAlert: Boolean
        get() = sharePreference.settingAllowFireAlert

    val settingAllowPump: Boolean
        get() = sharePreference.settingAllowPump

    val settingAllowNotification: Boolean
        get() = sharePreference.settingAllowNotification

    val settingAllowNotiSound: Boolean
        get() = sharePreference.settingAllowNotiSound

    val settingAllowNotiVibrate: Boolean
        get() = sharePreference.settingAllowNotiVibrate
}