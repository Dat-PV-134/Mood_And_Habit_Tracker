package com.rekoj134.moodandhabittracker.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.features.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 4000)
    }
}