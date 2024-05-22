package com.rekoj134.moodandhabittracker.presentation.settings

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

class LanguagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvLanguage.withModels {
            itemLanguage {
                id(0)
                isSelected(true)
                flag(R.drawable.img_english)
                languageName("English")
            }
            itemLanguage {
                id(1)
                isSelected(false)
                flag(R.drawable.img_vietnam)
                languageName("Vietnamese")
            }
            itemLanguage {
                id(2)
                isSelected(false)
                flag(R.drawable.img_hindi)
                languageName("Hindi")
            }
            itemLanguage {
                id(3)
                isSelected(false)
                flag(R.drawable.img_japan)
                languageName("Japanese")
            }
            itemLanguage {
                id(4)
                isSelected(false)
                flag(R.drawable.img_french)
                languageName("French")
            }
            itemLanguage {
                id(5)
                isSelected(false)
                flag(R.drawable.img_german)
                languageName("German")
            }
            itemLanguage {
                id(6)
                isSelected(false)
                flag(R.drawable.img_portuge)
                languageName("Portugal")
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

