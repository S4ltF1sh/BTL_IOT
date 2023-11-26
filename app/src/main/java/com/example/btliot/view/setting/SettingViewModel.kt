package com.example.btliot.view.setting

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.btliot.MyApp
import com.example.btliot.R
import com.example.btliot.common.UserType
import com.example.btliot.databinding.FragmentSettingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    private val isEditing = MutableLiveData(false)

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository?.start()
        }
    }

    suspend fun observeData(
        viewBinding: FragmentSettingBinding,
        lifecycleOwner: LifecycleOwner
    ) {
        settingRepository?.userLocation?.observe(lifecycleOwner) { userLocation ->
            viewBinding.settingInformation.edtUserLocation.setText(userLocation)
        }

        settingRepository?.temperatureAutoAirConditioner?.observe(lifecycleOwner) { temperature ->
            viewBinding.settingInformation.edtTemperatureAutoAirConditioner.setText(temperature.toString())
        }

        settingRepository?.temperatureAlert?.observe(lifecycleOwner) { temperature ->
            viewBinding.settingInformation.edtTemperatureAlert.setText(temperature.toString())
        }

        settingRepository?.gasAlert?.observe(lifecycleOwner) { gasRate ->
            viewBinding.settingInformation.edtGasAlert.setText(gasRate.toString())
        }

        settingRepository?.allowAutoAirConditioner?.observe(lifecycleOwner) { isAutoAirConditioner ->
            viewBinding.settingFunction.swAutoAirConditioner.isChecked = isAutoAirConditioner
        }

        settingRepository?.allowNotification?.observe(lifecycleOwner) { isNotification ->
            viewBinding.settingNotification.swNotification.isChecked = isNotification
        }

        settingRepository?.allowNotiSound?.observe(lifecycleOwner) { isNotiSound ->
            viewBinding.settingNotification.swSound.isChecked = isNotiSound
        }

        settingRepository?.allowNotiVibrate?.observe(lifecycleOwner) { isNotiVibrate ->
            viewBinding.settingNotification.swVibration.isChecked = isNotiVibrate
        }

//        isEditing.observe(lifecycleOwner) { isEditing ->
//            viewBinding.toolbar.menu.findItem(R.id.btnDone).isVisible = isEditing
//        }
    }

    fun setOnUserChangeSetting(mBinding: FragmentSettingBinding) {
//        mBinding.settingFunction.apply {
//            swAutoAirConditioner.setOnCheckedChangeListener { _, isChecked ->
//                isEditing.postValue((isEditing.value != true) && (isChecked != settingRepository?.allowAutoAirConditioner?.value))
//            }
//        }
//
//        mBinding.settingNotification.apply {
//            swNotification.setOnCheckedChangeListener { _, isChecked ->
//                isEditing.postValue((isEditing.value != true) && (isChecked != settingRepository?.allowNotification?.value))
//            }
//
//            swSound.setOnCheckedChangeListener { _, isChecked ->
//                isEditing.postValue((isEditing.value != true) && (isChecked != settingRepository?.allowNotiSound?.value))
//            }
//
//            swVibration.setOnCheckedChangeListener { _, isChecked ->
//                isEditing.postValue((isEditing.value != true) && (isChecked != settingRepository?.allowNotiVibrate?.value))
//            }
//        }
    }

    fun setBackButton(mBinding: FragmentSettingBinding, onClick: () -> Unit) {
        mBinding.toolbar.setNavigationOnClickListener {
            onClick()
        }
    }

    fun setInformationActions(mBinding: FragmentSettingBinding) {
        mBinding.settingInformation.apply {
            if (settingRepository?.getUserType() == UserType.ADMIN) {
                tilGasAlert.visibility = View.VISIBLE
                tilTemperatureAlert.visibility = View.VISIBLE
                tilUserLocation.visibility = View.GONE
                tilTemperatureAutoAirConditioner.visibility = View.GONE
            }

            //edtUserLocation.inputType = EditorInfo.TYPE_NULL

            btnSaveChange.setOnClickListener {
                edtUserLocation.clearFocus()
                edtTemperatureAlert.clearFocus()
                edtGasAlert.clearFocus()
                edtTemperatureAutoAirConditioner.clearFocus()

                settingRepository?.updateInformation(
                    edtUserLocation.text.toString(),
                    edtTemperatureAlert.text.toString().toFloat(),
                    edtGasAlert.text.toString().toFloat(),
                    edtTemperatureAutoAirConditioner.text.toString().toFloat()
                )
            }
        }
    }

    fun setUpdateSettingButton(mBinding: FragmentSettingBinding) {
        mBinding.apply {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.btnDone -> {
                        updateSettings(
                            isAutoAirConditioner = settingFunction.swAutoAirConditioner.isChecked,
                            isNotification = settingNotification.swNotification.isChecked,
                            isNotiSound = settingNotification.swSound.isChecked,
                            isNotiVibrate = settingNotification.swVibration.isChecked,
                        )
                        true
                    }

                    else -> false
                }
            }
            if (settingRepository?.getUserType() == UserType.ADMIN) {
                settingFunction.root.visibility = View.GONE
            }
        }
    }

    private fun updateSettings(
        isAutoAirConditioner: Boolean,
        isNotification: Boolean,
        isNotiSound: Boolean,
        isNotiVibrate: Boolean,
    ) {
        settingRepository?.updateSettings(
            isAutoAirConditioner,
            isNotification,
            isNotiSound,
            isNotiVibrate,
        )

        isEditing.postValue(false)
    }
}