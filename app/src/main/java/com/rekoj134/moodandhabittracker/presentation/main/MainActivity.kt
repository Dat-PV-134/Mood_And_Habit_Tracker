package com.rekoj134.moodandhabittracker.presentation.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.rekoj134.moodandhabittracker.ItemRoutineTaskBindingModel_
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.databinding.ActivityMainBinding
import com.rekoj134.moodandhabittracker.itemRoutineTask
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.presentation.routines.RoutinesFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyPreferences.init(this@MainActivity)
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

        }

        binding.btnFocusTime.setOnClickListener {

        }

        binding.btnDiary.setOnClickListener {

        }

        binding.btnStatistic.setOnClickListener {

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        RoutinesFragment.setOfTaskCompleteToday.removeObservers(this@MainActivity)
        super.onDestroy()
    }
}
