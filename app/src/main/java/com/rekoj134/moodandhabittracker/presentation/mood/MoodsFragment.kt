package com.rekoj134.moodandhabittracker.presentation.mood

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
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
import com.rekoj134.moodandhabittracker.constant.TYPE_DELETE
import com.rekoj134.moodandhabittracker.constant.TYPE_EDIT
import com.rekoj134.moodandhabittracker.constant.TYPE_HISTORY
import com.rekoj134.moodandhabittracker.databinding.FragmentMoodsBinding
import com.rekoj134.moodandhabittracker.databinding.ItemCalendarDayBinding
import com.rekoj134.moodandhabittracker.databinding.PopupMoodBinding
import com.rekoj134.moodandhabittracker.databinding.PopupRoutineBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.itemMood
import com.rekoj134.moodandhabittracker.model.Mood
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.setTextColorRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

class MoodsFragment : BaseFragment() {
    private var _binding: FragmentMoodsBinding? = null
    private val binding get() = _binding

    private var curMood = -1

    private val monthCalendarView: CalendarView? get() = binding?.monthCalendar
    private val perfectDates = mutableSetOf<LocalDate>()
    private val goodDates = mutableSetOf<LocalDate>()
    private val normalDates = mutableSetOf<LocalDate>()
    private val badDates = mutableSetOf<LocalDate>()
    private val terribleDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private var selectedDate = LocalDate.now()

    private val listMood by lazy { ArrayList<Mood>() }
    private val listMoodOfDaySelected by lazy { ArrayList<Mood>() }

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
        handleEvent()
    }

    private fun selectMood() {
        binding?.imgPerfect?.setImageResource(if (curMood == EMOTION_PERFECT) R.drawable.ic_perfect_selected else R.drawable.ic_perfect)
        binding?.imgGood?.setImageResource(if (curMood == EMOTION_GOOD) R.drawable.ic_good_selected else R.drawable.ic_good)
        binding?.imgNormal?.setImageResource(if (curMood == EMOTION_NORMAL) R.drawable.ic_normal_selected else R.drawable.ic_normal)
        binding?.imgBad?.setImageResource(if (curMood == EMOTION_BAD) R.drawable.ic_bad_selected else R.drawable.ic_bad)
        binding?.imgTerrible?.setImageResource(if (curMood == EMOTION_TERRIBLE) R.drawable.ic_terrible_selected else R.drawable.ic_terrible)
    }

    private fun handleEvent() {
        binding?.btnTerrible?.setOnClickListener {
            curMood = if (curMood != EMOTION_TERRIBLE) EMOTION_TERRIBLE else -1
            selectMood()
        }

        binding?.btnBad?.setOnClickListener {
            curMood = if (curMood != EMOTION_BAD) EMOTION_BAD else -1
            selectMood()
        }

        binding?.btnNormal?.setOnClickListener {
            curMood = if (curMood != EMOTION_NORMAL) EMOTION_NORMAL else -1
            selectMood()
        }

        binding?.btnGood?.setOnClickListener {
            curMood = if (curMood != EMOTION_GOOD) EMOTION_GOOD else -1
            selectMood()
        }

        binding?.btnPerfect?.setOnClickListener {
            curMood = if (curMood != EMOTION_PERFECT) EMOTION_PERFECT else -1
            selectMood()
        }

        binding?.btnToday?.setOnClickListener {
            dateClicked(today)
        }

        binding?.btnAddNewMood?.setOnClickListener {
            binding?.etFeeling?.let {
                if (it.text.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.leave_your_feeling), Toast.LENGTH_SHORT).show()
                } else if (curMood == -1) {
                    Toast.makeText(requireContext(), getString(R.string.select_your_mood), Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val calendar = Calendar.getInstance()
                        val hour = calendar.get(Calendar.HOUR_OF_DAY)
                        val minute = calendar.get(Calendar.MINUTE)
                        val second = calendar.get(Calendar.SECOND)
                        val newMood = Mood(
                            0,
                            curMood,
                            Timeline(0, LocalDateUtil.fromDateToString(LocalDate.now()), hour, minute, second),
                            binding?.etFeeling?.text.toString()
                        )
                        val newId = MyDatabase.getInstance(requireContext()).moodDao().insertMood(
                            newMood
                        )
                        val tempMood = Mood(
                            newId.toInt(),
                            newMood.mood,
                            newMood.date,
                            newMood.feeling
                        )
                        listMood.add(0, tempMood)
                        withContext(Dispatchers.Main) {
                            binding?.etFeeling?.setText("")
                            curMood = -1
                            selectMood()
                            dateClicked(today)
                            Toast.makeText(requireContext(), getString(R.string.added_new_mood_successfully), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun initData() {
        binding?.layoutTop?.visibility = View.GONE
        binding?.svContent?.visibility = View.GONE
        binding?.pbLoading?.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            listMood.clear()
            listMood.addAll(MyDatabase.getInstance(requireContext()).moodDao().getAllMoodSorted())
            for (item in listMood) {
                if (item.mood == EMOTION_PERFECT) perfectDates.add(LocalDateUtil.fromStringToDate(item.date.date))
                if (item.mood == EMOTION_GOOD) goodDates.add(LocalDateUtil.fromStringToDate(item.date.date))
                if (item.mood == EMOTION_NORMAL) normalDates.add(LocalDateUtil.fromStringToDate(item.date.date))
                if (item.mood == EMOTION_BAD) badDates.add(LocalDateUtil.fromStringToDate(item.date.date))
                if (item.mood == EMOTION_TERRIBLE) terribleDates.add(LocalDateUtil.fromStringToDate(item.date.date))
            }
            withContext(Dispatchers.Main) {
                setupView()
            }
        }
    }

    private fun setupView() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)
        val daysOfWeek = daysOfWeek()
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        binding?.tvToday?.text = today.dayOfMonth.toString()

        binding?.rvMoods?.withModels {
            listMoodOfDaySelected.forEach {
                itemMood {
                    id(it.id)
                    mood(it.mood)
                    feeling(it.feeling)
                    time(it.date.hour.toString() + ":" + it.date.minute)
                    onClickMenu { view ->
                        showPopupMore(view) { type ->
                            when (type) {
                                TYPE_EDIT -> {

                                }

                                TYPE_DELETE -> {
                                    deleteMood(it)
                                }
                            }
                        }
                    }
                }
            }
        }

        binding?.layoutTop?.visibility = View.VISIBLE
        binding?.svContent?.visibility = View.VISIBLE
        binding?.pbLoading?.visibility = View.GONE

        updateListMoodOnSelectedDay()
    }

    private fun deleteMood(mood: Mood) {
        CoroutineScope(Dispatchers.IO).launch {
            MyDatabase.getInstance(requireContext()).moodDao().deleteMood(mood)
            listMood.remove(mood)
            listMoodOfDaySelected.remove(mood)
            binding?.rvMoods?.requestModelBuild()
        }
    }

    private fun showPopupMore(view: View, onDone: (Int) -> Unit) {
        val popupInflater =
            requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = PopupMoodBinding.inflate(popupInflater)

        val popupWindow = PopupWindow(
            popupBind.root,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply { contentView.setOnClickListener { dismiss() } }

        popupBind.btnEdit.setOnClickListener {
            onDone(TYPE_EDIT)
            popupWindow.dismiss()
        }

        popupBind.btnDelete.setOnClickListener {
            onDone(TYPE_DELETE)
            popupWindow.dismiss()
        }

        popupWindow.isClippingEnabled = false
        popupWindow.showAsDropDown(view, -200, -50)
    }

    private fun updateListMoodOnSelectedDay() {
        CoroutineScope(Dispatchers.IO).launch {
            listMoodOfDaySelected.clear()
            for (item in listMood) {
                if (LocalDateUtil.fromStringToDate(item.date.date) == selectedDate) listMoodOfDaySelected.add(item)
            }
            withContext(Dispatchers.Main) {
                binding?.rvMoods?.requestModelBuild()
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
                        dateClicked(date = day.date)
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

    private fun dateClicked(date: LocalDate) {
        val temp = selectedDate
        selectedDate = date
        monthCalendarView?.notifyDateChanged(temp)
        monthCalendarView?.notifyDateChanged(selectedDate)
        updateListMoodOnSelectedDay()
    }

    private fun bindDateNew(date: LocalDate, tvUnselect: TextView, tvDate: TextView, imageViewStatus: ImageView, isSelectable: Boolean, isBefore: Boolean) {
        tvUnselect.text = date.dayOfMonth.toString()
        tvDate.text = date.dayOfMonth.toString()

        if (isSelectable && !isBefore) {
            when {
                perfectDates.contains(date) -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_perfect_selected else R.drawable.ic_perfect)
                goodDates.contains(date) -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_good_selected else R.drawable.ic_good)
                normalDates.contains(date) -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_normal_selected else R.drawable.ic_normal)
                badDates.contains(date) -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_bad_selected else R.drawable.ic_bad)
                terribleDates.contains(date) -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_terrible_selected else R.drawable.ic_terrible)
                else -> imageViewStatus.setImageResource(if (selectedDate == date) R.drawable.ic_no_data_selected else R.drawable.ic_no_data)
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
