package com.example.btliot.view.home.chart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.btliot.MyApp
import com.example.btliot.common.StatusLevel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChartRepository {
    private val mTemperatureRecord = MutableLiveData(0f)
    val temperatureRecord = mTemperatureRecord

    private val mGasRecord = MutableLiveData(0)
    val gasRecord = mGasRecord

    private val mIsFire = MutableLiveData(false)
    val isFire = mIsFire

    private val mIsGasLeak = MutableLiveData(false)
    val isGasLeak = mIsGasLeak

    interface UpdateDataListener {
        fun onUpdateTemperature(temperature: Float)
        fun onUpdateGasRate(gasRate: Int)
        fun onUpdateIsFire(isFire: Boolean)
        fun onUpdateIsGasLeak(isGasLeak: Boolean)
    }

    suspend fun start(
        updateDataListener: UpdateDataListener? = null
    ) {
        val firebaseDatabase = Firebase.database.reference
        val database = MyApp.getInstance().database

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
    }

    private suspend fun addDatabaseListener(
        firebaseDatabase: DatabaseReference,
        updateDataListener: UpdateDataListener?
    ) {
        firebaseDatabase.child("data/temperature")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val newTemperature = snapshot.value.toString().toFloat()
                    Log.d("DATA_FROM_FIREBASE", "temperature change: $newTemperature\n-----------")
                    mTemperatureRecord.postValue(newTemperature)
                    updateDataListener?.onUpdateTemperature(newTemperature)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })

        firebaseDatabase.child("data/gasRate").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newGasRate = snapshot.value.toString().toInt()
                Log.d("DATA_FROM_FIREBASE", "gasRate change: $newGasRate\n-----------")
                mGasRecord.postValue(newGasRate)
                updateDataListener?.onUpdateGasRate(newGasRate)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

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

    fun getStatusString(
        isFire: Boolean = mIsFire.value == true,
        isGasLeak: Boolean = mIsGasLeak.value == true
    ): String {
        val temperatureStatus = if (isFire) "On Fire" else when (temperatureRecord.value ?: 0f) {
            in 0f..30f -> "Normal"
            in 31f..40f -> "Hot"
            in 41f..45f -> "Warning"
            else -> "Danger"
        }

        val gasRateStatus = if (isGasLeak) "Gas Leak" else when (gasRecord.value ?: 0) {
            in 0..100 -> "Normal"
            in 101..150 -> "Warning"
            else -> "Danger"
        }

        return "Temperature: $temperatureStatus\nGas: $gasRateStatus"
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
            in 31f..40f -> StatusLevel.WARNING
            in 41f..45f -> StatusLevel.DANGER
            else -> StatusLevel.DANGER
        }
    }

    fun getGasRateStatus(
        isGasLeak: Boolean = mIsGasLeak.value == true,
        gas: Int = gasRecord.value ?: 0
    ): StatusLevel {
        if (isGasLeak) return StatusLevel.DANGER

        return when (gas) {
            in 0..100 -> StatusLevel.NORMAL
            in 101..150 -> StatusLevel.WARNING
            else -> StatusLevel.DANGER
        }
    }
}