package com.rekoj134.moodandhabittracker.presentation.journal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.airbnb.epoxy.EpoxyModel.SpanSizeOverrideCallback
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.constant.EXTRAS_READ
import com.rekoj134.moodandhabittracker.constant.EXTRAS_WRITE
import com.rekoj134.moodandhabittracker.constant.EXTRA_JOURNAL
import com.rekoj134.moodandhabittracker.constant.EXTRA_JOURNAL_OBJECT
import com.rekoj134.moodandhabittracker.databinding.FragmentJournalBinding
import com.rekoj134.moodandhabittracker.databinding.PopupFilterRoutineBinding
import com.rekoj134.moodandhabittracker.databinding.PopupJournalBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.itemJournal
import com.rekoj134.moodandhabittracker.itemLoadingFull
import com.rekoj134.moodandhabittracker.itemNone
import com.rekoj134.moodandhabittracker.model.Journal
import com.rekoj134.moodandhabittracker.model.JournalContent
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.presentation.focus.FocusInstance
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalFragment : BaseFragment() {
    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding
    private val listJournals = ArrayList<Journal>()

    private var isLoading = true
    private var isNewestFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupView()
        handleEvent()
    }

    private fun setupView() {
        binding?.rvRoutines?.withModels {
            if (isLoading) {
                itemLoadingFull {
                    id("loading")
                    spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
                }
            } else {
                if (listJournals.isEmpty()) {
                    itemNone {
                        id("none")
                        spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount }
                    }
                } else {
                    listJournals.forEach {
                        itemJournal {
                            id(it.id)
                            date(getDate(it.date))
                            content(getContent(it.content))
                            emotion(it.mood)
                            onClick { _ ->
                                val intent = Intent(requireContext(), JournalDetailActivity::class.java)
                                intent.putExtra(EXTRA_JOURNAL, EXTRAS_READ)
                                intent.putExtra(EXTRA_JOURNAL_OBJECT, it)
                                customJournalLauncher.launch(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    private val customJournalLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            listJournals.clear()
            binding?.rvRoutines?.requestModelBuild()
            initData()
        }
    }

    private fun getDate(date: Timeline): String {
        return date.date.replace("-", "/") + " - " + getFormattedTime(date.hour, date.minute)
    }

    private fun getFormattedTime(hour: Int, minutes: Int) : String {
        val strHour = if (hour < 10) "0$hour" else hour.toString()
        val strMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
        if (hour <= 12) return "$strHour:$strMinutes" + " am"
        else return "$strHour:$strMinutes" + " pm"
    }

    private fun getContent(journalContent: String): String {
        val content = ModelConverterUtil.fromStringToListContent(journalContent)
        return ModelConverterUtil.fromStringToText(content[0].data).text
    }

    private fun initData() {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            listJournals.clear()
            if (isNewestFirst) listJournals.addAll(MyDatabase.getInstance(requireContext()).journalDao().getAllJournalSorted())
            else listJournals.addAll(MyDatabase.getInstance(requireContext()).journalDao().getAllJournal())
            withContext(Dispatchers.Main) {
                isLoading = false
                binding?.rvRoutines?.requestModelBuild()
            }
        }
    }

    private fun handleEvent() {
        binding?.btnAdd?.setOnClickListener {
            val intent = Intent(requireContext(), JournalDetailActivity::class.java)
            intent.putExtra(EXTRA_JOURNAL, EXTRAS_WRITE)
            customJournalLauncher.launch(intent)
        }

        binding?.btnSort?.setOnClickListener {
            showPopupSort(it)
        }
    }

    private fun showPopupSort(view: View) {
        val popupInflater = requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = PopupJournalBinding.inflate(popupInflater)

        val popupWindow = PopupWindow(
            popupBind.root,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply { contentView.setOnClickListener { dismiss() } }

        if (isNewestFirst) popupBind.imgOldest.visibility = View.INVISIBLE
        else popupBind.imgNewest.visibility = View.INVISIBLE

        popupBind.btnOldest.setOnClickListener {
            isNewestFirst = false
            initData()
            popupWindow.dismiss()
        }

        popupBind.btnNewest.setOnClickListener {
            isNewestFirst = true
            initData()
            popupWindow.dismiss()
        }

        popupWindow.isClippingEnabled = false
        popupWindow.showAsDropDown(view, -184, -50)
    }

    override fun onDestroy() {
        Log.e("ANCUTKO", "Destroyed")
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("bind:emotion")
fun setEmotion(view: ImageView, emotion: Int) {
    when(emotion) {
        EMOTION_TERRIBLE -> view.setImageResource(R.drawable.ic_terrible)
        EMOTION_BAD -> view.setImageResource(R.drawable.ic_bad)
        EMOTION_NORMAL -> view.setImageResource(R.drawable.ic_normal)
        EMOTION_GOOD -> view.setImageResource(R.drawable.ic_good)
        EMOTION_PERFECT -> view.setImageResource(R.drawable.ic_perfect)
    }
}

@BindingAdapter("bind:background_emotion")
fun setBackgroundEmotion(view: ConstraintLayout, emotion: Int) {
    when(emotion) {
        EMOTION_TERRIBLE -> view.setBackgroundResource(R.drawable.bg_item_journal_terrible)
        EMOTION_BAD -> view.setBackgroundResource(R.drawable.bg_item_journal_bad)
        EMOTION_NORMAL -> view.setBackgroundResource(R.drawable.bg_item_journal_normal)
        EMOTION_GOOD -> view.setBackgroundResource(R.drawable.bg_item_journal_good)
        EMOTION_PERFECT -> view.setBackgroundResource(R.drawable.bg_item_journal_perfect)
    }
}