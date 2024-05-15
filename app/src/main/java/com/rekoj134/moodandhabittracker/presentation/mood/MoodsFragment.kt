package com.rekoj134.moodandhabittracker.presentation.mood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.databinding.FragmentMoodsBinding
import com.rekoj134.moodandhabittracker.databinding.ItemCalendarDayBinding
import com.rekoj134.moodandhabittracker.util.setTextColorRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MoodsFragment : BaseFragment() {
    private var _binding: FragmentMoodsBinding? = null
    private val binding get() = _binding

    private val monthCalendarView: CalendarView? get() = binding?.monthCalendar
    private val completeDates = mutableSetOf<LocalDate>()
    private var selectedDate = LocalDate.now()
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
        setupView()
    }

    private fun setupView() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        binding?.tvToday?.text = today.dayOfMonth.toString()
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

    override fun onDestroy() {
        Log.e("ANCUTKO", "Destroyed")
        super.onDestroy()
        _binding = null
    }
}
