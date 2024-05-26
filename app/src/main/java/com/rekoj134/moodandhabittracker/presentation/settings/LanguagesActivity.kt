package com.rekoj134.moodandhabittracker.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.databinding.ActivityLanguagesBinding
import com.rekoj134.moodandhabittracker.itemLanguage
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.presentation.main.MainActivity
import com.rekoj134.moodandhabittracker.util.LanguageUtil

class LanguagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding

    private var curLanguage = MyPreferences.read(MyPreferences.PREF_LANGUAGE, "en")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDone.setOnClickListener {
            MyPreferences.write(MyPreferences.PREF_LANGUAGE, curLanguage!!)
            LanguageUtil.setLanguage(this@LanguagesActivity)
            val intent = Intent(this@LanguagesActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvLanguage.withModels {
            itemLanguage {
                id(0)
                isSelected(curLanguage == "en")
                flag(R.drawable.img_english)
                languageName("English")
                onClickLanguage { _ ->
                    curLanguage = "en"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(1)
                isSelected(curLanguage == "vi")
                flag(R.drawable.img_vietnam)
                languageName("Vietnamese")
                onClickLanguage { _ ->
                    curLanguage = "vi"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(2)
                isSelected(curLanguage == "hi")
                flag(R.drawable.img_hindi)
                languageName("Hindi")
                onClickLanguage { _ ->
                    curLanguage = "hi"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(3)
                isSelected(curLanguage == "ja")
                flag(R.drawable.img_japan)
                languageName("Japanese")
                onClickLanguage { _ ->
                    curLanguage = "ja"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(4)
                isSelected(curLanguage == "fr")
                flag(R.drawable.img_french)
                languageName("French")
                onClickLanguage { _ ->
                    curLanguage = "fr"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(5)
                isSelected(curLanguage == "de")
                flag(R.drawable.img_german)
                languageName("German")
                onClickLanguage { _ ->
                    curLanguage = "de"
                    binding.rvLanguage.requestModelBuild()
                }
            }
            itemLanguage {
                id(6)
                isSelected(curLanguage == "pt")
                flag(R.drawable.img_portuge)
                languageName("Portugal")
                onClickLanguage { _ ->
                    curLanguage = "pt"
                    binding.rvLanguage.requestModelBuild()
                }
            }
        }
    }
}

@BindingAdapter("bind:language_icon")
fun setLanguageIcon(view: ImageView, resource: Int) {
    view.setImageResource(resource)
}

@BindingAdapter("bind:language_background")
fun setLanguageBackground(view: ConstraintLayout, isSelected: Boolean) {
    if (isSelected) view.setBackgroundResource(R.drawable.bg_language)
    else view.setBackgroundResource(R.drawable.bg_language_selected)
}

