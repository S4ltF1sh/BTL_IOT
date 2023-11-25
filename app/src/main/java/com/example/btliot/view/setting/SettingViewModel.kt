package com.example.btliot.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.btliot.MyApp
import com.example.btliot.R
import com.example.btliot.common.Strings
import com.example.btliot.databinding.FragmentSettingBinding

class SettingViewModel(private val settingRepository: SettingRepository?) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingViewModel(MyApp.getInstance().appContainer.settingRepository) as T
                }
            }
    }

    fun updateSettings(
        newUserLocation: String,
        newUserPhoneNumber: String,
        newFireFighterPhoneNumber: String,
        isFireAlert: Boolean,
        isPump: Boolean,
        isNotification: Boolean,
        isNotiSound: Boolean,
        isNotiVibrate: Boolean,
    ) {
        settingRepository?.updateSettings(
            newUserLocation,
            newUserPhoneNumber,
            newFireFighterPhoneNumber,
            isFireAlert,
            isPump,
            isNotification,
            isNotiSound,
            isNotiVibrate,
        )
    }

    fun setDefaultSettings(mBinding: FragmentSettingBinding) {
        mBinding.apply {
            settingInformation.edtUserLocation.setText(
                settingRepository?.userLocation ?: Strings.EMPTY
            )
            settingInformation.edtUserPhoneNumber.setText(
                settingRepository?.userPhoneNumber ?: Strings.EMPTY
            )
            settingInformation.edtFireFighterPhoneNumber.setText(
                settingRepository?.fireFighterPhoneNumber ?: Strings.EMPTY
            )

            settingFunction.swFireAlert.isChecked = settingRepository?.settingAllowFireAlert == true
            settingFunction.swPump.isChecked = settingRepository?.settingAllowPump == true
            settingNotification.swNotification.isChecked =
                settingRepository?.settingAllowNotification == true
            settingNotification.swSound.isChecked = settingRepository?.settingAllowNotiSound == true
            settingNotification.swVibration.isChecked =
                settingRepository?.settingAllowNotiVibrate == true
        }
    }

    fun setBackButton(mBinding: FragmentSettingBinding, onClick: () -> Unit) {
        mBinding.toolbar.setNavigationOnClickListener {
            onClick()
        }
    }

    fun setUpdateSettingButton(mBinding: FragmentSettingBinding) {
        mBinding.apply {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.btnDone -> {
                        updateSettings(
                            newUserLocation = settingInformation.edtUserLocation.text.toString(),
                            newUserPhoneNumber = settingInformation.edtUserPhoneNumber.text.toString(),
                            newFireFighterPhoneNumber = settingInformation.edtFireFighterPhoneNumber.text.toString(),
                            isFireAlert = settingFunction.swFireAlert.isChecked,
                            isPump = settingFunction.swPump.isChecked,
                            isNotification = settingNotification.swNotification.isChecked,
                            isNotiSound = settingNotification.swSound.isChecked,
                            isNotiVibrate = settingNotification.swVibration.isChecked,
                        )
                        true
                    }

                    else -> false
                }
            }
        }
    }
}