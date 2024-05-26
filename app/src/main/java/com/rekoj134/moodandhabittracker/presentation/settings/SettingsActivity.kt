package com.rekoj134.moodandhabittracker.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.moodandhabittracker.R
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

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRate.setOnClickListener {
            rateApp(this@SettingsActivity)
        }

        binding.btnFeedback.setOnClickListener {
            feedBack(this@SettingsActivity, packageName, "rekoj1342@gmail.com", "")
        }

        binding.btnShare.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/html"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var shareMessage = getString(R.string.app_name)
            shareMessage = "${shareMessage}https://play.google.com/store/apps/details?id=com.applock.fingerprintlock.passwordlock.patternlock.photovault.lockapps.pincode.hiddenfiles.hidephotos.hidevideo.hidedocuments"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    private fun feedBack(activity: Context, subject: String?, email: String?, body: String?) {
        try {
            val uriText = String.format("mailto:%s?subject=%s&body=%s", email, subject, body)
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriText))
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rateApp(context: Context) {
        try {
            val intent = Intent("android.intent.action.VIEW")
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.applock.fingerprintlock.passwordlock.patternlock.photovault.lockapps.pincode.hiddenfiles.hidephotos.hidevideo.hidedocuments")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.vending")
            context.startActivity(intent)
        } catch (e: Exception) {
            // Do nothing
        }
    }
}