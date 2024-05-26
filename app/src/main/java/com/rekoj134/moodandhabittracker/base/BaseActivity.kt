package com.rekoj134.moodandhabittracker.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.moodandhabittracker.util.LanguageUtil

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtil.setLanguage(this@BaseActivity)
    }
}