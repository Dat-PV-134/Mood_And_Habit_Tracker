package com.rekoj134.moodandhabittracker.presentation.journal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.databinding.ActivityJournalDetailBinding
import com.rekoj134.moodandhabittracker.itemJournalDetailHeader
import com.rekoj134.moodandhabittracker.itemJournalDetailText

class JournalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalDetailBinding

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
            }
        }
    }
}