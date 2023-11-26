package com.example.btliot.view.setting

import androidx.lifecycle.MutableLiveData
import com.example.btliot.common.CommonSharePreference
import com.example.btliot.common.UserType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingRepository {
    private val sharePreference = CommonSharePreference.getInstance()
    private var firebaseDatabase: DatabaseReference? = null

    private val mUserLocation = MutableLiveData("")
    val userLocation = mUserLocation

    private val mTemperatureAlert = MutableLiveData(50f)
    val temperatureAlert = mTemperatureAlert

    private val mGasAlert = MutableLiveData(50f)
    val gasAlert = mGasAlert

    private val mTemperatureAutoAirConditioner = MutableLiveData(36f)
    val temperatureAutoAirConditioner = mTemperatureAutoAirConditioner

    private val mAllowAutoAirConditioner = MutableLiveData(false)
    val allowAutoAirConditioner = mAllowAutoAirConditioner

    private val mAllowNotification = MutableLiveData(false)
    val allowNotification = mAllowNotification

    private val mAllowNotiSound = MutableLiveData(false)
    val allowNotiSound = mAllowNotiSound

    private val mAllowNotiVibrate = MutableLiveData(false)
    val allowNotiVibrate = mAllowNotiVibrate

    private var isStarted = false

    interface UpdateSettingListener {
        fun onUpdateUserLocation(userLocation: String)
        fun onUpdateTemperatureAlert(temperatureAlert: Float)
        fun onUpdateGasAlert(gasAlert: Float)
        fun onUpdateTemperatureAutoAirConditioner(temperatureAutoAirConditioner: Float)
        fun onUpdateAllowAutoAirConditioner(allowAutoAirConditioner: Boolean)
        fun onUpdateAllowNotification(allowNotification: Boolean)
        fun onUpdateAllowNotiSound(allowNotiSound: Boolean)
        fun onUpdateAllowNotiVibrate(allowNotiVibrate: Boolean)
    }

    suspend fun start(
        updateSettingListener: UpdateSettingListener? = null
    ) {
        //if (isStarted) return
        //isStarted = true

        firebaseDatabase = Firebase.database.reference
        initData(firebaseDatabase)
        addDatabaseListener(firebaseDatabase, updateSettingListener)
    }

    private fun initData(firebaseDatabase: DatabaseReference?) {
        firebaseDatabase?.child("option/userLocation")?.get()?.addOnSuccessListener { snapshot ->
            val newUserLocation = snapshot.value?.toString() ?: sharePreference.userLocation
            mUserLocation.postValue(newUserLocation)
            sharePreference.userLocation = newUserLocation
        }

        firebaseDatabase?.child("Arduino/temperatureAlert")?.get()
            ?.addOnSuccessListener { snapshot ->
                val newTemperatureAlert =
                    snapshot.value?.toString()?.toFloat() ?: sharePreference.temperatureFireAlert
                mTemperatureAlert.postValue(newTemperatureAlert)
                sharePreference.temperatureFireAlert = newTemperatureAlert
            }

        firebaseDatabase?.child("Arduino/gasRateAlert")?.get()?.addOnSuccessListener { snapshot ->
            val newGasAlert = snapshot.value?.toString()?.toFloat() ?: sharePreference.gasAlert
            mGasAlert.postValue(newGasAlert)
            sharePreference.gasAlert = newGasAlert
        }

        firebaseDatabase?.child("Arduino/temperatureAirConditioner")?.get()
            ?.addOnSuccessListener { snapshot ->
                val newTemperatureAutoAirConditioner = snapshot.value?.toString()?.toFloat()
                    ?: sharePreference.temperatureAutoAirConditioner
                mTemperatureAutoAirConditioner.postValue(newTemperatureAutoAirConditioner)
                sharePreference.temperatureAutoAirConditioner = newTemperatureAutoAirConditioner
            }

        firebaseDatabase?.child("option/isAutoAirCondition")?.get()
            ?.addOnSuccessListener { snapshot ->
                val newAllowAutoAirConditioner = snapshot.value?.toString()?.toBoolean()
                    ?: sharePreference.settingAllowAutoAirConditioner
                mAllowAutoAirConditioner.postValue(newAllowAutoAirConditioner)
                sharePreference.settingAllowAutoAirConditioner = newAllowAutoAirConditioner
            }

        firebaseDatabase?.child("option/isNoti")?.get()?.addOnSuccessListener { snapshot ->
            val newAllowNotification =
                snapshot.value?.toString()?.toBoolean() ?: sharePreference.settingAllowNotification
            mAllowNotification.postValue(newAllowNotification)
            sharePreference.settingAllowNotification = newAllowNotification
        }

        firebaseDatabase?.child("option/isNotiSound")?.get()?.addOnSuccessListener { snapshot ->
            val newAllowNotiSound =
                snapshot.value?.toString()?.toBoolean() ?: sharePreference.settingAllowNotiSound
            mAllowNotiSound.postValue(newAllowNotiSound)
            sharePreference.settingAllowNotiSound = newAllowNotiSound
        }

        firebaseDatabase?.child("option/isNotiVibration")?.get()?.addOnSuccessListener { snapshot ->
            val newAllowNotiVibrate =
                snapshot.value?.toString()?.toBoolean() ?: sharePreference.settingAllowNotiVibrate
            mAllowNotiVibrate.postValue(newAllowNotiVibrate)
            sharePreference.settingAllowNotiVibrate = newAllowNotiVibrate
        }
    }

    private fun addDatabaseListener(
        firebaseDatabase: DatabaseReference?,
        updateSettingListener: UpdateSettingListener?
    ) {
        firebaseDatabase?.child("option/userLocation")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newUserLocation = snapshot.value?.toString() ?: sharePreference.userLocation
                    mUserLocation.postValue(newUserLocation)
                    sharePreference.userLocation = newUserLocation
                    updateSettingListener?.onUpdateUserLocation(newUserLocation)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("Arduino/temperatureAlert")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newTemperatureAlert = snapshot.value?.toString()?.toFloat()
                        ?: sharePreference.temperatureFireAlert
                    mTemperatureAlert.postValue(newTemperatureAlert)
                    sharePreference.temperatureFireAlert = newTemperatureAlert
                    updateSettingListener?.onUpdateTemperatureAlert(newTemperatureAlert)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("Arduino/gasRateAlert")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newGasAlert =
                        snapshot.value?.toString()?.toFloat() ?: sharePreference.gasAlert
                    mGasAlert.postValue(newGasAlert)
                    sharePreference.gasAlert = newGasAlert
                    updateSettingListener?.onUpdateGasAlert(newGasAlert)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("Arduino/temperatureAirConditioner")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newTemperatureAutoAirConditioner = snapshot.value?.toString()?.toFloat()
                        ?: sharePreference.temperatureAutoAirConditioner
                    mTemperatureAutoAirConditioner.postValue(newTemperatureAutoAirConditioner)
                    sharePreference.temperatureAutoAirConditioner = newTemperatureAutoAirConditioner
                    updateSettingListener?.onUpdateTemperatureAutoAirConditioner(
                        newTemperatureAutoAirConditioner
                    )
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("option/isAutoAirCondition")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newAllowAutoAirConditioner = snapshot.value?.toString()?.toBoolean()
                        ?: sharePreference.settingAllowAutoAirConditioner
                    mAllowAutoAirConditioner.postValue(newAllowAutoAirConditioner)
                    sharePreference.settingAllowAutoAirConditioner = newAllowAutoAirConditioner
                    updateSettingListener?.onUpdateAllowAutoAirConditioner(
                        newAllowAutoAirConditioner
                    )
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("option/isNoti")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newAllowNotification = snapshot.value?.toString()?.toBoolean()
                        ?: sharePreference.settingAllowNotification
                    mAllowNotification.postValue(newAllowNotification)
                    sharePreference.settingAllowNotification = newAllowNotification
                    updateSettingListener?.onUpdateAllowNotification(newAllowNotification)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("option/isNotiSound")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newAllowNotiSound = snapshot.value?.toString()?.toBoolean()
                        ?: sharePreference.settingAllowNotiSound
                    mAllowNotiSound.postValue(newAllowNotiSound)
                    sharePreference.settingAllowNotiSound = newAllowNotiSound
                    updateSettingListener?.onUpdateAllowNotiSound(newAllowNotiSound)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        firebaseDatabase?.child("option/isNotiVibration")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newAllowNotiVibrate = snapshot.value?.toString()?.toBoolean()
                        ?: sharePreference.settingAllowNotiVibrate
                    mAllowNotiVibrate.postValue(newAllowNotiVibrate)
                    sharePreference.settingAllowNotiVibrate = newAllowNotiVibrate
                    updateSettingListener?.onUpdateAllowNotiVibrate(newAllowNotiVibrate)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
    }

    fun updateInformation(
        userLocation: String,
        temperatureAlert: Float,
        gasAlert: Float,
        temperatureAutoAirConditioner: Float
    ) {
        updateInformationLocal(
            userLocation,
            temperatureAlert,
            gasAlert,
            temperatureAutoAirConditioner
        )

        updateInformationFirebase(
            userLocation,
            temperatureAlert,
            gasAlert,
            temperatureAutoAirConditioner
        )
    }

    private fun updateInformationLocal(
        userLocation: String,
        temperatureAlert: Float,
        gasAlert: Float,
        temperatureAutoAirConditioner: Float
    ) {
        sharePreference.userLocation = userLocation
        sharePreference.temperatureFireAlert = temperatureAlert
        sharePreference.gasAlert = gasAlert
        sharePreference.temperatureAutoAirConditioner = temperatureAutoAirConditioner
    }

    private fun updateInformationFirebase(
        userLocation: String,
        temperatureAlert: Float,
        gasAlert: Float,
        temperatureAutoAirConditioner: Float
    ) {
        setUserLocation(userLocation)
        setTemperatureAlert(temperatureAlert)
        setGasAlert(gasAlert)
        setTemperatureAirConditioner(temperatureAutoAirConditioner)
    }

    fun updateSettings(
        autoAirConditioner: Boolean,
        notification: Boolean,
        notiSound: Boolean,
        notiVibrate: Boolean
    ) {
        updateSettingsLocal(
            autoAirConditioner,
            notification,
            notiSound,
            notiVibrate
        )

        updateSettingFirebase(
            autoAirConditioner,
            notification,
            notiSound,
            notiVibrate
        )
    }

    private fun updateSettingsLocal(
        autoAirConditioner: Boolean,
        notification: Boolean,
        notiSound: Boolean,
        notiVibrate: Boolean
    ) {
        sharePreference.settingAllowAutoAirConditioner = autoAirConditioner
        sharePreference.settingAllowNotification = notification
        sharePreference.settingAllowNotiSound = notiSound
        sharePreference.settingAllowNotiVibrate = notiVibrate
    }

    private fun updateSettingFirebase(
        autoAirConditioner: Boolean,
        notification: Boolean,
        notiSound: Boolean,
        notiVibrate: Boolean
    ) {
        setAllowAutoAirConditioner(autoAirConditioner)
        setAllowNotification(notification)
        setAllowNotiSound(notiSound)
        setAllowNotiVibrate(notiVibrate)
    }

    fun setUserLocation(value: String) {
        firebaseDatabase?.child("option/userLocation")?.setValue(value)
    }

    private fun setTemperatureAlert(value: Float) {
        firebaseDatabase?.child("Arduino/temperatureAlert")?.setValue(value)
    }

    private fun setGasAlert(value: Float) {
        firebaseDatabase?.child("Arduino/gasRateAlert")?.setValue(value)
    }

    private fun setTemperatureAirConditioner(value: Float) {
        firebaseDatabase?.child("Arduino/temperatureAirConditioner")?.setValue(value)
    }

    private fun setAllowAutoAirConditioner(value: Boolean) {
        firebaseDatabase?.child("option/isAutoAirCondition")?.setValue(value)
    }

    private fun setAllowNotification(value: Boolean) {
        firebaseDatabase?.child("option/isNoti")?.setValue(value)
    }

    private fun setAllowNotiSound(value: Boolean) {
        firebaseDatabase?.child("option/isNotiSound")?.setValue(value)
    }

    private fun setAllowNotiVibrate(value: Boolean) {
        firebaseDatabase?.child("option/isNotiVibration")?.setValue(value)
    }

    fun getUserType(): UserType {
        return if (sharePreference.userName == "admin") {
            UserType.ADMIN
        } else {
            UserType.USER
        }
    }
}