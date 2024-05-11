package com.rekoj134.moodandhabittracker.features.main

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
import com.rekoj134.moodandhabittracker.features.routines.RoutinesFragment
import com.rekoj134.moodandhabittracker.itemRoutineTask

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnRoutines.setOnClickListener {
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
}

@BindingAdapter("bind:visibility")
fun setVisibility(view: EpoxyRecyclerView, isExpand: Boolean) {
    if (isExpand) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("bind:data")
fun setData(view: Carousel, data: List<String>) {
    Log.e("ANCUTKO", data.toString())
    view.withModels {
        data.forEach {
            ItemRoutineTaskBindingModel_().id(it.hashCode()).task(it).addTo(this@withModels)
        }
    }
}
