package com.rekoj134.moodandhabittracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.rekoj134.moodandhabittracker.databinding.DialogEditFeelingBinding
import com.rekoj134.moodandhabittracker.databinding.DialogEditFocusTimeBinding
import com.rekoj134.moodandhabittracker.preference.MyPreferences

class CustomTimeDialog(
    context: Context,
    onClickOk:(Long, Long, Long) -> Unit,
    onClickNo:() -> Unit
) : Dialog(context) {
    private var binding = DialogEditFocusTimeBinding.inflate(layoutInflater)

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        val window = this.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.etFocus.setText(millisecondsToMinutes(MyPreferences.read(MyPreferences.PREF_FOCUS_TIME, 1500000L)).toString())
        binding.etBreak.setText(millisecondsToMinutes(MyPreferences.read(MyPreferences.PREF_BREAK_TIME, 300000L)).toString())
        binding.etLongBreak.setText(millisecondsToMinutes(MyPreferences.read(MyPreferences.PREF_LONG_BREAK_TIME, 900000L)).toString())

        binding.btnNo.setOnClickListener {
            onClickNo()
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            onClickOk(
                minutesToMilliSecond(binding.etFocus.text.toString().toLong()),
                minutesToMilliSecond(binding.etBreak.text.toString().toLong()),
                minutesToMilliSecond(binding.etLongBreak.text.toString().toLong())
            )
            dismiss()
        }
    }

    private fun minutesToMilliSecond(minutes: Long) : Long {
        return minutes * 60 * 1000L
    }

    private fun millisecondsToMinutes(milliseconds: Long): Long {
        return milliseconds / 60000
    }
}