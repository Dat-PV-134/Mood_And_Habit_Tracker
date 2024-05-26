package com.rekoj134.moodandhabittracker.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.databinding.ActivityMainBinding
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.presentation.focus.FocusTimeActivity
import com.rekoj134.moodandhabittracker.presentation.journal.JournalFragment
import com.rekoj134.moodandhabittracker.presentation.mood.MoodsFragment
import com.rekoj134.moodandhabittracker.presentation.routines.RoutinesFragment
import com.rekoj134.moodandhabittracker.presentation.stat.StatisticsFragment
import com.rekoj134.moodandhabittracker.service.FocusService

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        handleEvent()
    }

    private fun setupView() {
        replaceFragment(RoutinesFragment())
    }

    private fun handleEvent() {
        binding.btnRoutines.setOnClickListener {
            RoutinesFragment.setOfTaskCompleteToday.removeObservers(this@MainActivity)
            RoutinesFragment.currentChangeRoutine.removeObservers(this@MainActivity)
            replaceFragment(RoutinesFragment())
        }

        binding.btnMoods.setOnClickListener {
            replaceFragment(MoodsFragment())
        }

        binding.btnFocusTime.setOnClickListener {
            startActivity(Intent(this@MainActivity, FocusTimeActivity::class.java))
        }

        binding.btnDiary.setOnClickListener {
            replaceFragment(JournalFragment())
        }

        binding.btnStatistic.setOnClickListener {
            replaceFragment(StatisticsFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        RoutinesFragment.setOfTaskCompleteToday.removeObservers(this@MainActivity)
        stopForegroundService()
        super.onDestroy()
    }

    private fun stopForegroundService() {
        val intent = Intent(this@MainActivity, FocusService::class.java)
        stopService(intent)
    }
}

