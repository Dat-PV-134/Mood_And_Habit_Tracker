package com.rekoj134.moodandhabittracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.rekoj134.moodandhabittracker.databinding.DialogSelectLabelBinding

class SelectLabelDialog(
    context: Context,
    onClickOk:() -> Unit,
    onClickNo:() -> Unit
) : Dialog(context) {
    private var binding = DialogSelectLabelBinding.inflate(layoutInflater)

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        val window = this.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.btnNo.setOnClickListener {
            onClickNo()
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            onClickOk()
            dismiss()
        }

        binding.btnCreateNew.setOnClickListener {
            dismiss()
        }
    }
}