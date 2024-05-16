package com.rekoj134.moodandhabittracker.presentation.focus

import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.databinding.ActivityFocusTimeBinding
import com.rekoj134.moodandhabittracker.itemPomodoro

class FocusTimeActivity : BaseActivity() {
    private lateinit var binding: ActivityFocusTimeBinding
    private val currentTime by lazy { Calendar.getInstance() }

    private val handler by lazy { Handler(Looper.getMainLooper()) }
    val runnable = object : Runnable {
        override fun run() {
            currentTime.timeInMillis = Calendar.getInstance().timeInMillis
            binding.clock.setTime(
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                currentTime.get(Calendar.SECOND)
            )
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvPomodoro.withModels {
            itemPomodoro {
                id("test")
            }
        }
    }

    private fun setupView() {
        setupClockView()
    }

    private fun setupClockView() {
        handler.postDelayed(runnable, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}