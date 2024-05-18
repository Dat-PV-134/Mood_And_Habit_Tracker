package com.rekoj134.moodandhabittracker.presentation.mood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.databinding.FragmentMoodsBinding
import com.rekoj134.moodandhabittracker.databinding.ItemCalendarDayBinding
import com.rekoj134.moodandhabittracker.itemMood
import com.rekoj134.moodandhabittracker.model.Mood
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.setTextColorRes
import java.sql.Time
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MoodsFragment : BaseFragment() {
    private var _binding: FragmentMoodsBinding? = null
    private val binding get() = _binding

    private val monthCalendarView: CalendarView? get() = binding?.monthCalendar
    private val perfectDates = mutableSetOf<LocalDate>()
    private val goodDates = mutableSetOf<LocalDate>()
    private val normalDates = mutableSetOf<LocalDate>()
    private val badDates = mutableSetOf<LocalDate>()
    private val terribleDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoodsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupView()
    }

    private fun initData() {
        perfectDates.add(today)
        perfectDates.add(today.minusDays(1))
        perfectDates.add(today.minusDays(2))
        perfectDates.add(today.minusDays(5))
        perfectDates.add(today.minusDays(7))
        perfectDates.add(today.minusDays(9))

        goodDates.add(today.minusDays(4))
        goodDates.add(today.minusDays(8))
        goodDates.add(today.minusDays(16))

        normalDates.add(today.minusDays(15))
        goodDates.add(today.minusDays(11))

        badDates.add(today.minusDays(17))
        badDates.add(today.minusDays(10))
        badDates.add(today.minusDays(14))

        terribleDates.add(today.minusDays(13))
    }

    private fun setupView() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        binding?.tvToday?.text = today.dayOfMonth.toString()

        val listMood = ArrayList<Mood>()
        listMood.add(Mood(1, EMOTION_PERFECT, Timeline(1, "18-05-2024", 8, 26, 0), "Feeling perfect after eat"))
        listMood.add(Mood(0, EMOTION_BAD, Timeline(0, "18-05-2024", 6, 30, 0), "Feeling bad"))

        binding?.rvMoods?.withModels {
            listMood.forEach {
                itemMood {
                    id(it.id)
                    mood(it.mood)
                    feeling(it.feeling)
                    time(it.date.hour.toString() + ":" + it.date.minute)
                }
            }
        }
    }

    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val tvUnselect = ItemCalendarDayBinding.bind(view).exOneDayText
            val tvDate = ItemCalendarDayBinding.bind(view).tvDay
            val imgState = ItemCalendarDayBinding.bind(view).imgStatus

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
//                        dateClicked(date = day.date)
                    }
                }
            }
        }
        monthCalendarView?.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDateNew(data.date, container.tvUnselect, container.tvDate, container.imgState, data.position == DayPosition.MonthDate && data.date <= today, false)
            }
        }
        monthCalendarView?.monthScrollListener = {
//            updateTitle()
        }
        monthCalendarView?.setup(startMonth, endMonth, daysOfWeek.first())
        monthCalendarView?.scrollToMonth(currentMonth)
    }

    private fun bindDateNew(date: LocalDate, tvUnselect: TextView, tvDate: TextView, imageViewStatus: ImageView, isSelectable: Boolean, isBefore: Boolean) {
        tvUnselect.text = date.dayOfMonth.toString()
        tvDate.text = date.dayOfMonth.toString()

        if (isSelectable && !isBefore) {
            when {
                perfectDates.contains(date) -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_perfect_selected else R.drawable.ic_perfect)
                goodDates.contains(date) -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_good_selected else R.drawable.ic_good)
                normalDates.contains(date) -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_normal_selected else R.drawable.ic_normal)
                badDates.contains(date) -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_bad_selected else R.drawable.ic_bad)
                terribleDates.contains(date) -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_terrible_selected else R.drawable.ic_terrible)
                else -> imageViewStatus.setImageResource(if (today == date) R.drawable.ic_no_data_selected else R.drawable.ic_no_data)
            }
        } else if (isBefore && isSelectable) {
            tvDate.setTextColorRes(R.color.black)
            imageViewStatus.setImageResource(if (today == date) R.drawable.ic_no_data_selected else R.drawable.ic_no_data)
        } else {
            tvDate.visibility = View.GONE
            imageViewStatus.visibility = View.GONE
            tvUnselect.visibility = View.VISIBLE
            tvUnselect.setTextColorRes(R.color.text_color_secondary)
            tvUnselect.background = null
        }
    }

    override fun onDestroy() {
        Log.e("ANCUTKO", "Destroyed")
        super.onDestroy()
        _binding = null
    }
}

@BindingAdapter("bind:set_mood")
fun setMood(view: ImageView, mood: Int) {
    when (mood) {
        EMOTION_TERRIBLE -> view.setImageResource(R.drawable.ic_terrible)
        EMOTION_BAD -> view.setImageResource(R.drawable.ic_bad)
        EMOTION_NORMAL -> view.setImageResource(R.drawable.ic_normal)
        EMOTION_GOOD -> view.setImageResource(R.drawable.ic_good)
        EMOTION_PERFECT -> view.setImageResource(R.drawable.ic_perfect)
    }
}
