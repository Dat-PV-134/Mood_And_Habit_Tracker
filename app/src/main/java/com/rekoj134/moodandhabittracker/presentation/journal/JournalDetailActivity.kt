package com.rekoj134.moodandhabittracker.presentation.journal

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.constant.EXTRAS_WRITE
import com.rekoj134.moodandhabittracker.constant.EXTRA_JOURNAL
import com.rekoj134.moodandhabittracker.constant.EXTRA_JOURNAL_OBJECT
import com.rekoj134.moodandhabittracker.constant.TYPE_TEXT
import com.rekoj134.moodandhabittracker.databinding.ActivityJournalDetailBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.dialog.SelectMoodDialog
import com.rekoj134.moodandhabittracker.itemJournalDetailHeader
import com.rekoj134.moodandhabittracker.itemJournalDetailText
import com.rekoj134.moodandhabittracker.model.Journal
import com.rekoj134.moodandhabittracker.model.JournalContent
import com.rekoj134.moodandhabittracker.model.Text
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Calendar

class JournalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalDetailBinding
    private var isNew = true
    private var isEnable = false
    private var curMood = EMOTION_PERFECT
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    private lateinit var curJournal: Journal
    private var isChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContent.withModels {
            itemJournalDetailHeader {
                id("Header")
                curMood(curMood)
                curDate(LocalDateUtil.fromDateToString(LocalDate.now()).replace("-", "/"))
                curTime(getFormattedTime(hour, minute))
                onClickMood { v ->
                    showDialogSelectMood()
                }
            }
        }
        setupView()
        handleEvent()
    }

    private fun getFormattedTime(hour: Int, minutes: Int) : String {
        val strHour = if (hour < 10) "0$hour" else hour.toString()
        val strMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
        if (hour <= 12) return "$strHour:$strMinutes" + " am"
        else return "$strHour:$strMinutes" + " pm"
    }

    private fun showDialogSelectMood() {
        val selectMoodDialog = SelectMoodDialog(this@JournalDetailActivity, {
            curMood = it
            binding.rvContent.requestModelBuild()
        }, {

        })
        selectMoodDialog.show()
    }

    private fun handleEvent() {
        binding.btnDone.setOnClickListener {
            if (isNew) saveNewJournal()
            else updateJournal()
        }

        binding.btnBack.setOnClickListener {
            if(isChange) setResult(RESULT_OK)
            finish()
        }

        binding.btnDelete.setOnClickListener {
            deleteJournal()
        }

        binding.btnEdit.setOnClickListener {
            setupViewWrite()
        }
    }

    private fun updateJournal() {
        CoroutineScope(Dispatchers.IO).launch {
            MyDatabase.getInstance(this@JournalDetailActivity).journalDao().updateJournal(
                Journal(
                    curJournal.id,
                    curJournal.date,
                    ModelConverterUtil.fromListContentToString(
                        arrayListOf(
                            JournalContent(
                                0, TYPE_TEXT, ModelConverterUtil.fromTextToString(
                                    Text(text = binding.etContent.text.toString())
                                )
                            )
                        )
                    ),
                    curMood
                )
            )
            isChange = true
            withContext(Dispatchers.Main) {
                setupViewRead()
            }
        }
    }

    private fun deleteJournal() {
        curJournal.let {
            CoroutineScope(Dispatchers.IO).launch {
                MyDatabase.getInstance(this@JournalDetailActivity).journalDao().deleteJournal(
                    it
                )
                withContext(Dispatchers.IO) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun saveNewJournal() {
        CoroutineScope(Dispatchers.IO).launch {
            MyDatabase.getInstance(this@JournalDetailActivity).journalDao().insertJournal(
                Journal(
                    0,
                    Timeline(0, LocalDateUtil.fromDateToString(LocalDate.now()), hour, minute, second),
                    ModelConverterUtil.fromListContentToString(
                        arrayListOf(
                            JournalContent(
                                0, TYPE_TEXT, ModelConverterUtil.fromTextToString(
                                    Text(text = binding.etContent.text.toString())
                                )
                            )
                        )
                    ),
                    curMood
                )
            )
            withContext(Dispatchers.IO) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupView() {
        intent?.let {
            if (it.getIntExtra(EXTRA_JOURNAL, EXTRAS_WRITE) == EXTRAS_WRITE) setupViewWrite()
            else {
                isNew = false
                curJournal = it.getSerializableExtra(EXTRA_JOURNAL_OBJECT) as Journal
                binding.etContent.setText(getContent(curJournal.content))
                curMood = curJournal.mood
                setupViewRead()
            }
        }
    }

    private fun setupViewRead() {
        binding.btnEdit.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnDone.visibility = View.GONE
        isEnable = false
        binding.rvContent.requestModelBuild()
        binding.etContent.isEnabled = isEnable
    }

    private fun getContent(journalContent: String): String {
        val content = ModelConverterUtil.fromStringToListContent(journalContent)
        return ModelConverterUtil.fromStringToText(content[0].data).text
    }

    private fun setupViewWrite() {
        binding.btnEdit.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE
        binding.btnDone.visibility = View.VISIBLE
        isEnable = true
        binding.rvContent.requestModelBuild()
        binding.etContent.isEnabled = isEnable
    }
}

@BindingAdapter("bind:cur_mood")
fun setCurMood(view: ImageView, curMood: Int) {
    when (curMood) {
        EMOTION_PERFECT -> view.setImageResource(R.drawable.ic_perfect)
        EMOTION_GOOD -> view.setImageResource(R.drawable.ic_good)
        EMOTION_NORMAL -> view.setImageResource(R.drawable.ic_normal)
        EMOTION_BAD -> view.setImageResource(R.drawable.ic_bad)
        EMOTION_TERRIBLE -> view.setImageResource(R.drawable.ic_terrible)
    }
}


