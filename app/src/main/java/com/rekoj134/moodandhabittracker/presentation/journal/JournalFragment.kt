package com.rekoj134.moodandhabittracker.presentation.journal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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
import com.rekoj134.moodandhabittracker.databinding.FragmentJournalBinding
import com.rekoj134.moodandhabittracker.itemJournal
import com.rekoj134.moodandhabittracker.model.Journal

class JournalFragment : BaseFragment() {
    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding
    private val listJournals = ArrayList<Journal>()

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
        listJournals.add(Journal(0, "13/04/2024 - 11:25 am", "I have a wonderful day and I want to share with you and my family. Let see my older brother doing little thing that make some body happy without a dog......", EMOTION_BAD))
        listJournals.add(Journal(1, "13/04/2024 - 11:25 am", "I have a wonderful day and I want to share with you and my family. Let see my older brother doing little thing that make some body happy without a dog......", EMOTION_PERFECT))
        listJournals.add(Journal(2, "13/04/2024 - 11:25 am", "I have a wonderful day and I want to share with you and my family. Let see my older brother doing little thing that make some body happy without a dog......", EMOTION_GOOD))
        listJournals.add(Journal(3, "13/04/2024 - 11:25 am", "I have a wonderful day and I want to share with you and my family. Let see my older brother doing little thing that make some body happy without a dog......", EMOTION_NORMAL))
        listJournals.add(Journal(4, "13/04/2024 - 11:25 am", "I have a wonderful day and I want to share with you and my family. Let see my older brother doing little thing that make some body happy without a dog......", EMOTION_TERRIBLE))
        binding?.rvRoutines?.withModels {
            listJournals.forEach {
                itemJournal {
                    id(it.id)
                    date(it.date)
                    content(it.content)
                    emotion(it.emotion)
                    onClick { _ ->
                        val intent = Intent(requireContext(), JournalDetailActivity::class.java)
                        intent.putExtra(EXTRA_JOURNAL, EXTRAS_READ)
                        startActivity(intent)
                    }
                }
            }
        }

        handleEvent()
    }

    private fun handleEvent() {
        binding?.btnAdd?.setOnClickListener {
            val intent = Intent(requireContext(), JournalDetailActivity::class.java)
            intent.putExtra(EXTRA_JOURNAL, EXTRAS_WRITE)
            startActivity(intent)
        }
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