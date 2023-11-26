package com.example.btliot.view.home.chart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.btliot.common.StatusLevel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class ChartRepository {
    private val mTemperatureRecord = MutableLiveData(0f)
    val temperatureRecord = mTemperatureRecord

    private val mGasRecord = MutableLiveData(0f)
    val gasRecord = mGasRecord

    private val mIsFire = MutableLiveData(false)
    val isFire = mIsFire

    private val mIsGasLeak = MutableLiveData(false)
    val isGasLeak = mIsGasLeak

    private val mIsAirConditionerOn = MutableLiveData(false)
    val isAirConditionerOn = mIsAirConditionerOn

    private val mIsAutoAirConditioner = MutableLiveData(false)
    val isAutoAirConditioner = mIsAutoAirConditioner

    private var isStarted = false
    var micIsOn = false

    interface UpdateDataListener {
        fun onUpdateTemperature(temperature: Float)
        fun onUpdateGasRate(gasRate: Float)
        fun onUpdateIsFire(isFire: Boolean)
        fun onUpdateIsGasLeak(isGasLeak: Boolean)
    }

    suspend fun start(
        updateDataListener: UpdateDataListener? = null
    ) {
//        if (isStarted) return
//        isStarted = true
        val firebaseDatabase = Firebase.database.reference

        initData(firebaseDatabase)
        addDatabaseListener(firebaseDatabase, updateDataListener)
    }

    private suspend fun initData(firebaseDatabase: DatabaseReference) {
//        firebaseDatabase.child("data/temperature").get().addOnSuccessListener { snapshot ->
//            val temperatureRecord: List<String> =
//                Gson().fromJson<List<String>>(snapshot.value.toString())
//                    .map { it.substring(it.indexOfFirst { c -> c == '=' }) }
//
//            val newTemperature = temperatureRecord.lastOrNull()?.toFloat()
//            Log.d("DATA_FROM_FIREBASE", "temperature init: $newTemperature\n-----------")
//            mTemperatureRecord.postValue(newTemperature)
//        }
//
//        firebaseDatabase.child("data/gasRate").get().addOnSuccessListener { snapshot ->
//            val gasRateRecord: List<String> =
//                Gson().fromJson<List<String>>(snapshot.value.toString())
//                    .map { it.substring(it.indexOfFirst { c -> c == '=' }) }
//
//            val newGasRate = gasRateRecord.lastOrNull()?.toInt()
//            Log.d("DATA_FROM_FIREBASE", "gasRate init: $newGasRate\n-----------")
//            mGasRecord.postValue(newGasRate)
//        }

        firebaseDatabase.child("Arduino/isFire").get().addOnSuccessListener { snapshot ->
            val isFire = snapshot.value.toString().toBoolean()
            Log.d("DATA_FROM_FIREBASE", "isFire init: $isFire\n-----------")
            mIsFire.postValue(isFire)
        }

        firebaseDatabase.child("Arduino/isGasLeak").get().addOnSuccessListener { snapshot ->
            val isGasLeak = snapshot.value.toString().toBoolean()
            Log.d("DATA_FROM_FIREBASE", "isGasLeak init: $isGasLeak\n-----------")
            mIsGasLeak.postValue(isGasLeak)
        }

        firebaseDatabase.child("Arduino/isAirConditionerOn").get()
            .addOnSuccessListener { snapshot ->
                val isAirConditionerOn = snapshot.value.toString().toBoolean()
                Log.d(
                    "DATA_FROM_FIREBASE",
                    "isAirConditionerOn init: $isAirConditionerOn\n-----------"
                )
                mIsAirConditionerOn.postValue(isAirConditionerOn)
            }

        firebaseDatabase.child("option/isAutoAirCondition").get()
            .addOnSuccessListener { snapshot ->
                val isAutoAirConditioner = snapshot.value.toString().toBoolean()
                Log.d(
                    "DATA_FROM_FIREBASE",
                    "isAutoAirConditioner init: $isAutoAirConditioner\n-----------"
                )
                mIsAutoAirConditioner.postValue(isAutoAirConditioner)
            }
    }

    private suspend fun addDatabaseListener(
        firebaseDatabase: DatabaseReference,
        updateDataListener: UpdateDataListener?
    ) {
        firebaseDatabase.child("data/temperature").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newTemperature = snapshot.value.toString().toFloat()
                    Log.d(
                        "DATA_FROM_FIREBASE",
                        "temperature change: $newTemperature\n-----------"
                    )
                    mTemperatureRecord.postValue(newTemperature)
                    updateDataListener?.onUpdateTemperature(newTemperature)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

        firebaseDatabase.child("data/gasRate").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var newGasRate = snapshot.value.toString().toFloat()
                    newGasRate = (newGasRate * 100).roundToInt() / 100f
                    Log.d("DATA_FROM_FIREBASE", "gasRate change: $newGasRate\n-----------")
                    mGasRecord.postValue(newGasRate)
                    updateDataListener?.onUpdateGasRate(newGasRate)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

        firebaseDatabase.child("Arduino/isFire").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isFire = snapshot.value.toString().toBoolean()
                    Log.d("DATA_FROM_FIREBASE", "isFire change: $isFire\n-----------")
                    mIsFire.postValue(isFire)
                    updateDataListener?.onUpdateIsFire(isFire)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

        firebaseDatabase.child("Arduino/isGasLeak").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isGasLeak = snapshot.value.toString().toBoolean()
                    Log.d("DATA_FROM_FIREBASE", "isGasLeak change: $isGasLeak\n-----------")
                    mIsGasLeak.postValue(isGasLeak)
                    updateDataListener?.onUpdateIsGasLeak(isGasLeak)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

        firebaseDatabase.child("Arduino/isAirConditionerOn").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isAirConditionerOn = snapshot.value.toString().toBoolean()
                    Log.d(
                        "DATA_FROM_FIREBASE",
                        "isAirConditionerOn change: $isAirConditionerOn\n-----------"
                    )
                    mIsAirConditionerOn.postValue(isAirConditionerOn)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

        firebaseDatabase.child("option/isAutoAirCondition").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isAutoAirConditioner = snapshot.value.toString().toBoolean()
                    Log.d(
                        "DATA_FROM_FIREBASE",
                        "isAutoAirConditioner change: $isAutoAirConditioner\n-----------"
                    )
                    mIsAutoAirConditioner.postValue(isAutoAirConditioner)
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )

//        database.child("data/test").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val gasRateRecordStr = snapshot.value.toString()
//                Log.d("DATA_FROM_FIREBASE", "test value change: $gasRateRecordStr\n-----------")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
    }

    fun setAirConditionerStatus(isOn: Boolean) {
        Firebase.database.reference.child("Arduino/isAirConditionerOn").setValue(isOn)
    }

    fun getStatusString(
        isFire: Boolean = mIsFire.value == true,
        isGasLeak: Boolean = mIsGasLeak.value == true
    ): String {
        val temperatureStatus = if (isFire) "On Fire" else when (temperatureRecord.value ?: 0f) {
            in 0f..30f -> "Normal"
            in 30.001f..40f -> "Hot"
            in 40.001f..45f -> "Warning"
            else -> "Danger"
        }

        val gasRateStatus = if (isGasLeak) "Gas Leak" else when (gasRecord.value ?: 0f) {
            in 0f..40f -> "Normal"
            in 40.001f..50f -> "Warning"
            else -> "Danger"
        }

        return "Temperature: $temperatureStatus\nSmoke: $gasRateStatus"
    }

    fun getAvgStatus(
        isFire: Boolean = mIsFire.value == true,
        isGasLeak: Boolean = mIsGasLeak.value == true
    ): StatusLevel {
        if (isFire || isGasLeak) return StatusLevel.DANGER

        val temperatureStatus = getTemperatureStatus()
        val gasRateStatus = getGasRateStatus()

        return if (temperatureStatus == StatusLevel.DANGER || gasRateStatus == StatusLevel.DANGER) StatusLevel.DANGER
        else if (temperatureStatus == StatusLevel.WARNING || gasRateStatus == StatusLevel.WARNING) StatusLevel.WARNING
        else StatusLevel.NORMAL
    }

    fun getTemperatureStatus(
        isFire: Boolean = mIsFire.value == true,
        temperature: Float = temperatureRecord.value ?: 0f
    ): StatusLevel {
        if (isFire) return StatusLevel.DANGER

        return when (temperature) {
            in 0f..30f -> StatusLevel.NORMAL
            in 30.001f..40f -> StatusLevel.WARNING
            in 40.001f..45f -> StatusLevel.DANGER
            else -> StatusLevel.DANGER
        }
    }

    fun getGasRateStatus(
        isGasLeak: Boolean = mIsGasLeak.value == true,
        gas: Float = gasRecord.value ?: 0f
    ): StatusLevel {
        if (isGasLeak) return StatusLevel.DANGER

        return when (gas) {
            in 0f..40f -> StatusLevel.NORMAL
            in 40.001f..50f -> StatusLevel.WARNING
            else -> StatusLevel.DANGER
        }
    }
}