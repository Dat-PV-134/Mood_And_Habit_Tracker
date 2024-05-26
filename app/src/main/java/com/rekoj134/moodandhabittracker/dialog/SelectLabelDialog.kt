package com.rekoj134.moodandhabittracker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.databinding.DialogSelectLabelBinding
import com.rekoj134.moodandhabittracker.itemLabel
import com.rekoj134.moodandhabittracker.model.Label
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil

class SelectLabelDialog(
    context: Context,
    onClickOk:(Label) -> Unit,
    onClickNo:() -> Unit
) : Dialog(context) {
    private var binding = DialogSelectLabelBinding.inflate(layoutInflater)
    private var curLabel = Label(0, context.getString(R.string.special), "#4D94CADB")

    private var listLabel = arrayListOf(
        Label(0, context.getString(R.string.special), "#4D94CADB"),
        Label(1, context.getString(R.string.work), "#4D8EC7BD"),
        Label(2, context.getString(R.string.study), "#4D98C7AB"),
        Label(3, context.getString(R.string.meditation), "#4DF5BB90"),
        Label(4, context.getString(R.string.unknown), "#4DFA9999"),
    )

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        val window = this.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        curLabel = ModelConverterUtil.fromStringToLabel(MyPreferences.read(MyPreferences.PREF_CURRENT_FOCUS_LABEL, ModelConverterUtil.fromLabelToString(Label(0, context.getString(R.string.special), "#4D94CADB")))!!)

        binding.rvLabel.withModels {
            listLabel.forEach {
                itemLabel {
                    id(it.id)
                    label(it.name)
                    backgroundColor(it.color)
                    isSelected(it.id == curLabel.id)
                    onClickItem { _ ->
                        curLabel = it
                        binding.rvLabel.requestModelBuild()
                    }
                }
            }
        }

        binding.btnNo.setOnClickListener {
            onClickNo()
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            onClickOk(curLabel)
            dismiss()
        }

        binding.btnCreateNew.setOnClickListener {
            dismiss()
        }
    }
}