package com.rekoj134.moodandhabittracker.presentation.journal

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.constant.EXTRAS_WRITE
import com.rekoj134.moodandhabittracker.constant.EXTRA_JOURNAL
import com.rekoj134.moodandhabittracker.constant.EXTRA_ROUTINE
import com.rekoj134.moodandhabittracker.databinding.ActivityJournalDetailBinding
import com.rekoj134.moodandhabittracker.itemJournalDetailHeader
import com.rekoj134.moodandhabittracker.itemJournalDetailText

class JournalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalDetailBinding
    private var isEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContent.withModels {
            itemJournalDetailHeader {
                id("Header")
            }
            itemJournalDetailText {
                id("text")
                isEnable(isEnable)
            }
        }
        setupView()
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnDone.setOnClickListener {

        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        intent?.let {
            if (it.getIntExtra(EXTRA_JOURNAL, EXTRAS_WRITE) == EXTRAS_WRITE) setupViewWrite()
            else setupViewRead()
        }
    }

    private fun setupViewRead() {
        binding.btnEdit.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnDone.visibility = View.GONE
        isEnable = false
        binding.rvContent.requestModelBuild()
    }

    private fun setupViewWrite() {
        binding.btnEdit.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE
        binding.btnDone.visibility = View.VISIBLE
        isEnable = true
        binding.rvContent.requestModelBuild()
    }
}

