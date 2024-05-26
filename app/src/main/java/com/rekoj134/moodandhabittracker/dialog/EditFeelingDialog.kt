package com.rekoj134.moodandhabittracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.Window
import com.rekoj134.moodandhabittracker.databinding.DialogConfirmDeleteBinding
import com.rekoj134.moodandhabittracker.databinding.DialogEditFeelingBinding

class EditFeelingDialog(
    context: Context,
    onClickOk:(String) -> Unit,
    onClickNo:() -> Unit
) : Dialog(context) {
    private var binding = DialogEditFeelingBinding.inflate(layoutInflater)

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        val window = this.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        binding.btnNo.setOnClickListener {
            onClickNo()
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            onClickOk(getFeeling())
            dismiss()
        }
    }

    fun setDefaultFeeling(feeling: String) {
        binding.etFeeling.setText(feeling)
    }

    private fun getFeeling() : String {
        return binding.etFeeling.text.toString()
    }
}