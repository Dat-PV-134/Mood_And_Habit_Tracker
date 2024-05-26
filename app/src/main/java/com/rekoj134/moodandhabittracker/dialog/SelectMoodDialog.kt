package com.rekoj134.moodandhabittracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.databinding.DialogSelectMoodBinding

class SelectMoodDialog(
    context: Context,
    onClickOk:(Int) -> Unit,
    onClickNo:() -> Unit
) : Dialog(context) {
    private var binding = DialogSelectMoodBinding.inflate(layoutInflater)

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        val window = this.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.btnPerfect.setOnClickListener {
            onClickOk(EMOTION_PERFECT)
            dismiss()
        }

        binding.btnGood.setOnClickListener {
            onClickOk(EMOTION_GOOD)
            dismiss()
        }

        binding.btnNormal.setOnClickListener {
            onClickOk(EMOTION_NORMAL)
            dismiss()
        }

        binding.btnBad.setOnClickListener {
            onClickOk(EMOTION_BAD)
            dismiss()
        }

        binding.btnTerrible.setOnClickListener {
            onClickOk(EMOTION_TERRIBLE)
            dismiss()
        }
    }
}