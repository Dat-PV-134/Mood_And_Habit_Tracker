package com.rekoj134.moodandhabittracker.presentation.focus

import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.databinding.ActivityFocusTimeBinding

class FocusTimeActivity : BaseActivity() {
    private lateinit var binding: ActivityFocusTimeBinding
    private val currentTime by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentTime.timeInMillis = Calendar.getInstance().timeInMillis
                var seconds= currentTime.timeInMillis / 1000
                val hours = (seconds / 3600)
                seconds = seconds % 3600
                val minutes = (seconds / 60)
                seconds = seconds % 60
                binding.clock.setTime(hours.toInt(), minutes.toInt(), seconds.toInt())
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 0)

    }
}