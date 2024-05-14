package com.rekoj134.moodandhabittracker.presentation.routines

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.constant.EXTRA_ROUTINE
import com.rekoj134.moodandhabittracker.databinding.ActivityRoutineDetailBinding
import com.rekoj134.moodandhabittracker.databinding.ItemCalendarDayBinding
import com.rekoj134.moodandhabittracker.model.Routine
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.setTextColorRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutineDetailBinding
    private var routine: Routine? = null

    private val monthCalendarView: CalendarView get() = binding.monthCalendar
    private val completeDates = mutableSetOf<LocalDate>()
    private var selectedDate = LocalDate.now()
    private val today = LocalDate.now()
    private var startDate = LocalDate.now()

    private var weekProgress = "0/7"
    private var monthProgress = "0/30"
    private var yearProgress = "0/365"

    private var weekProgressNumber = 0f
    private var monthProgressNumber = 0f
    private var yearProgressNumber = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setupView()
        handleEvent()
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnToday.setOnClickListener {
            monthCalendarView.scrollToMonth(YearMonth.now())
            dateClicked(today)
        }
    }

    private fun setupView() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        binding.tvToday.text = today.dayOfMonth.toString()
        binding.tvProgressWeek.text = weekProgress
        binding.tvProgressMonth.text = monthProgress
        binding.tvProgressYear.text = yearProgress
        binding.tvProgressAll.text = completeDates.size.toString()

        binding.pbProgressWeek.progress = weekProgressNumber
        binding.pbProgressMonth.progress = monthProgressNumber
        binding.pbProgressYear.progress = yearProgressNumber
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
                        dateClicked(date = day.date)
                    }
                }
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDateNew(data.date, container.tvUnselect, container.tvDate, container.imgState, data.position == DayPosition.MonthDate && data.date <= today, data.date < startDate)
            }
        }
        monthCalendarView.monthScrollListener = { updateTitle() }
        monthCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        monthCalendarView.scrollToMonth(currentMonth)
    }

    private fun updateTitle() {

    }

    private fun dateClicked(date: LocalDate) {
        val temp = selectedDate
        selectedDate = date
        monthCalendarView.notifyDateChanged(temp)
        monthCalendarView.notifyDateChanged(selectedDate)
    }

    private fun bindDateNew(date: LocalDate, tvUnselect: TextView, tvDate: TextView, imageViewStatus: ImageView, isSelectable: Boolean, isBefore: Boolean) {
        tvUnselect.text = date.dayOfMonth.toString()
        tvDate.text = date.dayOfMonth.toString()

        if (isSelectable && !isBefore) {
            when {
                completeDates.contains(date) -> {
                    tvDate.setTextColorRes(R.color.black)
                    imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_done_selected else R.drawable.ic_done)
                }
                today == date -> {
                    tvDate.setTextColorRes(R.color.black)
                    imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_no_data_selected else R.drawable.ic_no_data)
                }
                else -> {
                    tvDate.setTextColorRes(R.color.black)
                    imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_not_done_selected else R.drawable.ic_not_done)
                }
            }
        } else if (isBefore && isSelectable) {
            tvDate.setTextColorRes(R.color.black)
            imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_no_data_selected else R.drawable.ic_no_data)
        } else {
            tvDate.visibility = View.GONE
            imageViewStatus.visibility = View.GONE
            tvUnselect.visibility = View.VISIBLE
            tvUnselect.setTextColorRes(R.color.text_color_secondary)
            tvUnselect.background = null
        }
    }

    private fun initData() {
        routine = intent.extras?.getSerializable(EXTRA_ROUTINE) as Routine?
        routine?.let { routine ->
            startDate = LocalDateUtil.fromStringToDate(routine.startDate.date)
            routine.completeDates.forEach { completeDate ->
                completeDates.add(LocalDateUtil.fromStringToDate(completeDate.date))
            }
            initProgress(routine.completeDates)
        }
    }

    private fun initProgress(completeDates: List<Timeline>) {
        val completeDatesThisWeek = ArrayList<LocalDate>()
        val completeDatesThisMonth = ArrayList<LocalDate>()
        val completeDatesThisYear = ArrayList<LocalDate>()

        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val endOfWeek = startOfWeek.plusDays(6)

        val startOfMonth = today.withDayOfMonth(1)
        val endOfMonth = startOfMonth.plusMonths(1).minusDays(1)

        completeDates.forEach {
            if (LocalDateUtil.fromStringToDate(it.date).isAfter(startOfWeek) && LocalDateUtil.fromStringToDate(it.date).isBefore(endOfWeek)) completeDatesThisWeek.add(LocalDateUtil.fromStringToDate(it.date))
            if (LocalDateUtil.fromStringToDate(it.date).isAfter(startOfMonth) && LocalDateUtil.fromStringToDate(it.date).isBefore(endOfMonth)) completeDatesThisMonth.add(LocalDateUtil.fromStringToDate(it.date))
            if (LocalDateUtil.fromStringToDate(it.date).year == today.year) completeDatesThisYear.add(LocalDateUtil.fromStringToDate(it.date))
        }

        val daysInWeek = 7
        val daysInMonth = today.lengthOfMonth()
        val daysInYear = if (today.isLeapYear) 366 else 365

        weekProgress = completeDatesThisWeek.size.toString() + "/" + daysInWeek
        monthProgress = completeDatesThisMonth.size.toString() + "/" + daysInMonth
        yearProgress = completeDatesThisYear.size.toString() + "/" + daysInYear

        weekProgressNumber = completeDatesThisWeek.size / daysInWeek.toFloat() * 100
        monthProgressNumber = completeDatesThisMonth.size / daysInMonth.toFloat() * 100
        yearProgressNumber = completeDatesThisYear.size / daysInYear.toFloat() * 100

        if (completeDates.isEmpty()) binding.pbProgressAll.progress = 0f
    }
}