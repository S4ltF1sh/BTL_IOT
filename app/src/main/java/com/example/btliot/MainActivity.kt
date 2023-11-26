package com.example.btliot

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.btliot.common.Strings
import com.example.btliot.databinding.ActivityMainBinding
import com.example.btliot.notification.NotiManager
import com.example.btliot.util.PermissionUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>(factoryProducer = { MainActivityViewModel.Factory })

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var requestNotiPmsLauncher: ActivityResultLauncher<String>? = null
    private var requestRecordAudioPmsLauncher: ActivityResultLauncher<String>? = null
    private var requestLocationPmsLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        createRequestPmsLaunchers()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.start()

        viewModel.setNotificationHandler(
            this,
            object : NotiManager.NotiHandler {
                override fun onAdminNoti(
                    isNoti: Boolean,
                    userLocation: String,
                    isSound: Boolean,
                    isVibrate: Boolean
                ) {
                    NotiManager.getInstance()
                        .pushAdminNotification(userLocation, isSound, isVibrate)
                }

                override fun onUserNoti(
                    isNoti: Boolean,
                    content: String,
                    isSound: Boolean,
                    isVibrate: Boolean
                ) {
                    if (isNoti)
                        NotiManager.getInstance().pushUserNotification(content, isSound, isVibrate)
                }
            }
        )

        if (PermissionUtil.isGrantedLocationPermission(this)) {
            //getUserLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        if (!PermissionUtil.isGrantedLocationPermission(this)) {
            //requestLocationPermission()
        }

        if (!PermissionUtil.isGrantedPostNotificationPermission(this)) {
            requestNotiPermission()
        }

        if (!PermissionUtil.isGrantedRecordAudioPermission(this)) {
            requestRecordAudioPermission()
        }
    }

    private fun createRequestPmsLaunchers() {
        requestNotiPmsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                requestNotiPermission()
            }
        }

        requestRecordAudioPmsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                requestRecordAudioPermission()
            }
        }

        requestLocationPmsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (!permissions.values.contains(false)) {

            } else {
                //requestLocationPermission()
            }
        }
    }

    private fun requestNotiPermission() {
        requestNotiPmsLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun requestRecordAudioPermission() {
        requestRecordAudioPmsLauncher?.launch(Manifest.permission.RECORD_AUDIO)
    }

    private fun requestLocationPermission() {
        requestLocationPmsLauncher?.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val userLocation = "geo:${location.latitude},${location.longitude}"
            Log.i("MainActivity", "getUserLocation: ${location.latitude}, ${location.longitude}")
            viewModel.setUserLocation(userLocation)
        }
    }
}