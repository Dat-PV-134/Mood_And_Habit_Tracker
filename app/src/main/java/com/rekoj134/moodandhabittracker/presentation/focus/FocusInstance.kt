package com.rekoj134.moodandhabittracker.presentation.focus

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.rekoj134.moodandhabittracker.constant.TYPE_FOCUS
import com.rekoj134.moodandhabittracker.constant.TYPE_LONG_BREAK
import com.rekoj134.moodandhabittracker.constant.TYPE_SHORT_BREAK
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.model.FocusTime
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

object FocusInstance {
    var curLabel = "Unknown"
    var curColor = "#4D94CADB"

    var focusTime = 1500000L
    var totalFocusTime = 1500000L
    var breakTime = 300000L
    var totalBreakTime = 300000L
    var longBreakTime = 900000L
    var totalLongBreakTime = 900000L

    var curType = -1
    var curPercent = 0f
    var isPausing = false

    fun update(focus: Long, shortBreak: Long, longBreak: Long) {
        totalFocusTime = focus
        totalBreakTime = shortBreak
        totalLongBreakTime = longBreak

        if (curType != TYPE_FOCUS) focusTime = focus
        if (curType != TYPE_SHORT_BREAK) breakTime = shortBreak
        if (curType != TYPE_LONG_BREAK) longBreakTime = longBreak
    }

    private val handlerFocus by lazy { Handler(Looper.getMainLooper()) }
    private val runnableFocus = object : Runnable {
        override fun run() {
            if (isPausing) {
                handlerFocus.postDelayed(this, 1000)
                return
            }
            when (curType) {
                TYPE_LONG_BREAK -> {
                    if (longBreakTime <= 0) {
                        isPausing = false
                        longBreakTime = totalLongBreakTime
                        handlerFocus.removeCallbacks(this)
                        return
                    }
                    longBreakTime -= 1000L
                    curPercent = longBreakTime*100f/totalLongBreakTime
                }

                TYPE_FOCUS -> {
                    if (focusTime <= 0) {
                        isPausing = false
                        focusTime = totalFocusTime
                        handlerFocus.removeCallbacks(this)
                        return
                    }
                    focusTime -= 1000L
                    curPercent = focusTime*100f/totalFocusTime
                }

                TYPE_SHORT_BREAK -> {
                    if (breakTime <= 0) {
                        isPausing = false
                        breakTime = totalBreakTime
                        handlerFocus.removeCallbacks(this)
                        return
                    }
                    breakTime -= 1000L
                    curPercent = breakTime*100f/totalBreakTime
                }
            }
            if (curType == -1) return
            handlerFocus.postDelayed(this, 1000)
        }
    }

    fun saveFocus(context: Context) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        CoroutineScope(Dispatchers.IO).launch {
            MyDatabase.getInstance(context).focusDao().insertFocus(
                Focus(
                    0,
                    curLabel,
                    FocusTime(0, totalFocusTime - focusTime, ModelConverterUtil.fromTimelineToString(Timeline(
                        0,
                        LocalDateUtil.fromDateToString(LocalDate.now()),
                        hour,
                        minute,
                        second
                    ))),
                    curColor
                )
            )
        }
    }

    fun doFocus() {
        if (curType == TYPE_FOCUS) {
            isPausing = !isPausing
        } else {
            isPausing = false
            curType = -1
            handlerFocus.removeCallbacks(runnableFocus)
            breakTime = 300000L
            longBreakTime = 900000L
            curType = TYPE_FOCUS
            handlerFocus.post(runnableFocus)
        }
    }

    @SuppressLint("DefaultLocale")
    fun getFormattedFocusTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = (seconds % 3600) / 60
        val leftSecond = seconds - (60 * minutes)
        return String.format("%02d:%02d", minutes, leftSecond)
    }

    fun doShortBreak() {
        if (curType == TYPE_SHORT_BREAK) {
            isPausing = !isPausing
        } else {
            isPausing = false
            curType = -1
            handlerFocus.removeCallbacks(runnableFocus)
            focusTime = 1500000L
            longBreakTime = 900000L
            curType = TYPE_SHORT_BREAK
            handlerFocus.post(runnableFocus)
        }
    }

    fun doLongBreak() {
        if (curType == TYPE_LONG_BREAK) {
            isPausing = !isPausing
        } else {
            isPausing = false
            curType = -1
            handlerFocus.removeCallbacks(runnableFocus)
            focusTime = 1500000L
            breakTime = 300000L
            curType = TYPE_LONG_BREAK
            handlerFocus.post(runnableFocus)
        }
    }
}