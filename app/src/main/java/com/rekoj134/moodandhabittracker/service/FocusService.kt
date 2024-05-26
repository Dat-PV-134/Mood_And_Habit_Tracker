package com.rekoj134.moodandhabittracker.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.app.MyApplication
import com.rekoj134.moodandhabittracker.constant.TYPE_FOCUS
import com.rekoj134.moodandhabittracker.constant.TYPE_LONG_BREAK
import com.rekoj134.moodandhabittracker.constant.TYPE_SHORT_BREAK
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance
import com.rekoj134.moodandhabittracker.presentation.focus.FocusTimeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusService : Service() {
    private var serviceHandler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            pushNotification()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun pushNotification() {
        serviceHandler.postDelayed({
            pushNotification()
        }, 1000)

        if (!FocusInstance.isPausing) {
            val notificationIntent = this.packageManager.getLaunchIntentForPackage(packageName);
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val title = when (FocusInstance.curType) {
                TYPE_LONG_BREAK -> "Long break"
                TYPE_FOCUS -> "Focus"
                TYPE_SHORT_BREAK -> "break"
                else -> "Focus"
            }
            val content = when (FocusInstance.curType) {
                TYPE_LONG_BREAK -> FocusInstance.getFormattedFocusTime(FocusInstance.longBreakTime)
                TYPE_FOCUS ->  FocusInstance.getFormattedFocusTime(FocusInstance.longBreakTime)
                TYPE_SHORT_BREAK -> FocusInstance.getFormattedFocusTime(FocusInstance.breakTime)
                else -> "Focus"
            }

            val remainingTime = when (FocusInstance.curType) {
                TYPE_FOCUS -> FocusInstance.focusTime
                else -> -1
            }

            if (remainingTime <= 0) FocusInstance.saveFocus(applicationContext)

            when (FocusInstance.curType) {
                TYPE_LONG_BREAK -> {
                    if (FocusInstance.longBreakTime <= 0) {
                        FocusInstance.isPausing = false
                        FocusInstance.longBreakTime = FocusInstance.totalLongBreakTime
                    }
                }

                TYPE_FOCUS -> {
                    if (FocusInstance.focusTime <= 0) {
                        FocusInstance.isPausing = false
                        FocusInstance.focusTime = FocusInstance.totalFocusTime
                    }
                }

                TYPE_SHORT_BREAK -> {
                    if (FocusInstance.breakTime <= 0) {
                        FocusInstance.isPausing = false
                        FocusInstance.breakTime = FocusInstance.totalBreakTime
                    }
                }
            }

            val notification = NotificationCompat.Builder(this@FocusService, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_focus_time)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(134, notification)
        }
    }
}