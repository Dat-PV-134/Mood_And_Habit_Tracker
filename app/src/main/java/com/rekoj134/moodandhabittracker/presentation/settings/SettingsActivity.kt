package com.rekoj134.moodandhabittracker.presentation.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.moodandhabittracker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLanguages.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, LanguagesActivity::class.java))
        }
    }
}