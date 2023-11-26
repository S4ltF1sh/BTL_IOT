package com.example.btliot.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.btliot.MainActivity
import com.example.btliot.MyApp
import com.example.btliot.R

class NotiManager {
    companion object {
        const val CHANNEL_ID = "com.example.btliot.notification.CHANNEL_ID"

        private lateinit var instance: NotiManager
        fun getInstance(): NotiManager {
            if (!this::instance.isInitialized) {
                instance = NotiManager()
                return instance
            }
            return instance
        }
    }

    interface NotiHandler {
        fun onUserNoti(isNoti: Boolean, content: String, isSound: Boolean, isVibrate: Boolean)
        fun onAdminNoti(isNoti: Boolean, userLocation: String, isSound: Boolean, isVibrate: Boolean)
    }

    fun pushUserNotification(
        content: String,
        canPlaySound: Boolean = true,
        canVibrate: Boolean = true
    ) {
        val context = MyApp.context()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Warning")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .apply {
                if (canVibrate)
                    setVibrate(longArrayOf(10000, 10000, 10000, 10000, 10000))
            }
            .apply {
                if (canPlaySound)
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    fun pushAdminNotification(
        userLocation: String,
        canPlaySound: Boolean = true,
        canVibrate: Boolean = true
    ) {
        val gmmIntentUri = Uri.parse(userLocation)
        val context = MyApp.context()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mapIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("ALERT")
            .setContentText(userLocation)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .apply {
                if (canVibrate)
                    setVibrate(longArrayOf(10000, 10000, 10000, 10000, 10000))
            }
            .apply {
                if (canPlaySound)
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}