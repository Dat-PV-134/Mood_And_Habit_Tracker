package com.rekoj134.moodandhabittracker.presentation.focus

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.constant.TYPE_DELETE
import com.rekoj134.moodandhabittracker.constant.TYPE_EDIT
import com.rekoj134.moodandhabittracker.constant.TYPE_FOCUS
import com.rekoj134.moodandhabittracker.constant.TYPE_HISTORY
import com.rekoj134.moodandhabittracker.constant.TYPE_LONG_BREAK
import com.rekoj134.moodandhabittracker.constant.TYPE_SHORT_BREAK
import com.rekoj134.moodandhabittracker.databinding.ActivityFocusTimeBinding
import com.rekoj134.moodandhabittracker.databinding.PopupFocusBinding
import com.rekoj134.moodandhabittracker.databinding.PopupRoutineBinding
import com.rekoj134.moodandhabittracker.dialog.CustomTimeDialog
import com.rekoj134.moodandhabittracker.dialog.SelectLabelDialog
import com.rekoj134.moodandhabittracker.itemPomodoro
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.breakTime
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.curColor
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.curPercent
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.curType
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.doFocus
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.doLongBreak
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.doShortBreak
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.focusTime
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.longBreakTime
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance.updateLabel
import com.rekoj134.moodandhabittracker.service.FocusService
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FocusTimeActivity : BaseActivity() {
    private lateinit var binding: ActivityFocusTimeBinding
    private val currentTime by lazy { Calendar.getInstance() }

    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val runnable = object : Runnable {
        override fun run() {
            if (!isDestroyed) {
                currentTime.timeInMillis = Calendar.getInstance().timeInMillis
                binding.clock.setTime(
                    currentTime.get(Calendar.HOUR_OF_DAY),
                    currentTime.get(Calendar.MINUTE),
                    currentTime.get(Calendar.SECOND)
                )
                binding.clock.setPercent(curPercent, curColor)
                binding.rvPomodoro.requestModelBuild()
                handler.postDelayed(this, 1000)
            } else {
                handler.removeCallbacks(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMore.setOnClickListener {
            showMenuFocus(it)
        }
    }

    private fun showMenuFocus(view: View) {
        val popupInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = PopupFocusBinding.inflate(popupInflater)

        val popupWindow = PopupWindow(
            popupBind.root,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply { contentView.setOnClickListener { dismiss() } }

        popupBind.btnLabel.setOnClickListener {
            showDialogSelectLabel()
            popupWindow.dismiss()
        }

        popupBind.btnTime.setOnClickListener {
            showDialogCustomTime()
            popupWindow.dismiss()
        }

        popupWindow.isClippingEnabled = false
        popupWindow.showAsDropDown(view, -150, -50)
    }

    private fun showDialogSelectLabel() {
        val selectLabelDialog = SelectLabelDialog(this@FocusTimeActivity, {
            MyPreferences.write(MyPreferences.PREF_CURRENT_FOCUS_LABEL, ModelConverterUtil.fromLabelToString(it))
            updateLabel(it)
            Toast.makeText(this@FocusTimeActivity, getString(R.string.update_successfully), Toast.LENGTH_SHORT).show()
        }, {

        })
        selectLabelDialog.show()
    }

    private fun showDialogCustomTime() {
        val customTimeDialog = CustomTimeDialog(this@FocusTimeActivity, { focus, shortBreak, longBreak ->
            MyPreferences.write(MyPreferences.PREF_FOCUS_TIME, focus)
            MyPreferences.write(MyPreferences.PREF_BREAK_TIME, shortBreak)
            MyPreferences.write(MyPreferences.PREF_LONG_BREAK_TIME, longBreak)
            FocusInstance.update(focus, shortBreak, longBreak)
            binding.rvPomodoro.requestModelBuild()
            Toast.makeText(this@FocusTimeActivity, getString(R.string.update_successfully), Toast.LENGTH_SHORT).show()
        }, {

        })
        customTimeDialog.show()
    }

    private fun setupView() {
        setupClockView()
        setupViewFocus()
    }

    private fun setupViewFocus() {
        binding.rvPomodoro.withModels {
            itemPomodoro {
                id("long_break")
                time(millisecondsToTimeString(longBreakTime))
                onClickItem { _ ->
                    val intent = Intent(this@FocusTimeActivity, FocusService::class.java)
                    startService(intent)
                    if (curType == TYPE_FOCUS) {
                        FocusInstance.saveFocus(this@FocusTimeActivity) {
                            doLongBreak()
                        }
                    }
                }
            }

            itemPomodoro {
                id("focus_time")
                time(millisecondsToTimeString(focusTime))
                onClickItem { _ ->
                    val intent = Intent(this@FocusTimeActivity, FocusService::class.java)
                    startService(intent)
                    doFocus()
                }
            }

            itemPomodoro {
                id("break_time")
                time(millisecondsToTimeString(breakTime))
                onClickItem { _ ->
                    val intent = Intent(this@FocusTimeActivity, FocusService::class.java)
                    startService(intent)
                    if (curType == TYPE_FOCUS) {
                        FocusInstance.saveFocus(this@FocusTimeActivity) {
                            doShortBreak()
                        }
                    }
                }
            }
        }

        if (curType == -1) binding.rvPomodoro.scrollToPosition(1)
        else {
            when (curType) {
                TYPE_LONG_BREAK -> binding.rvPomodoro.scrollToPosition(0)
                TYPE_FOCUS -> binding.rvPomodoro.scrollToPosition(1)
                TYPE_SHORT_BREAK -> binding.rvPomodoro.scrollToPosition(2)
            }
        }
    }

    private fun setupClockView() {
        handler.postDelayed(runnable, 0)
    }

    @SuppressLint("DefaultLocale")
    fun millisecondsToTimeString(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = (seconds % 3600) / 60
        val leftSecond = seconds - (60 * minutes)
        return String.format("%02d:%02d", minutes, leftSecond)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}

@BindingAdapter("bind:is_selected_label")
fun setIsSelectedLabel(view: ImageView, isSelected: Boolean) {
    view.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("bind:set_background_tint")
fun setBackgroundTint(view: ConstraintLayout, color: String) {
    view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
}