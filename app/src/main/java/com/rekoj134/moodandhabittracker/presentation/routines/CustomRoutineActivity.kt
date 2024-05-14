package com.rekoj134.moodandhabittracker.presentation.routines

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyTouchHelper
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseActivity
import com.rekoj134.moodandhabittracker.constant.EXTRA_ROUTINE
import com.rekoj134.moodandhabittracker.constant.REPEAT_FRIDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_MONDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_SATURDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_SUNDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_THURSDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_TUESDAY
import com.rekoj134.moodandhabittracker.constant.REPEAT_WEDNESDAY
import com.rekoj134.moodandhabittracker.databinding.ActivityCustomRoutineBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.epoxy.controller.RoutineTasksController
import com.rekoj134.moodandhabittracker.model.Routine
import com.rekoj134.moodandhabittracker.model.RoutineTask
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.util.CalendarUtil
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.setTextColorRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

class CustomRoutineActivity : BaseActivity() {
    private lateinit var binding: ActivityCustomRoutineBinding
    private val setIdNotUse by lazy { HashSet<Int>() }
    private val controller by lazy { RoutineTasksController() }
    private var repeat = "2345678"
    private var notifyTime = 0L
    private val myCalendar by lazy { Calendar.getInstance() }
    private var curRoutine: Routine? = null
    private var isUpdate = false

    private var autoGenId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        curRoutine = intent.extras?.getSerializable(EXTRA_ROUTINE) as Routine?
        if (curRoutine != null) {
            isUpdate = true
            initCurrentData()
            setupViewUpdate()
        } else {
            initData()
        }
        setupView()
        handleEvent()
    }

    private fun initCurrentData() {
        curRoutine?.let {
            it.routineTasks.forEach { task ->
                setIdNotUse.add(task.id)
                controller.getListData().add(task)
            }
            repeat = it.repeat
            notifyTime = it.notifyTime
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupViewUpdate() {
        curRoutine?.let {
            binding.tvTitle.text = getString(R.string.edit_routine)

            binding.etRoutineName.setText(it.routineName)

            binding.tvRepeatMonday.setTextColorRes(if (repeat.contains(REPEAT_MONDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatMonday.setBackgroundResource(if (repeat.contains(REPEAT_MONDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatTuesday.setTextColorRes(if (repeat.contains(REPEAT_TUESDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatTuesday.setBackgroundResource(if (repeat.contains(REPEAT_TUESDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatWednesday.setTextColorRes(if (repeat.contains(REPEAT_WEDNESDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatWednesday.setBackgroundResource(if (repeat.contains(REPEAT_WEDNESDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatThursday.setTextColorRes(if (repeat.contains(REPEAT_THURSDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatThursday.setBackgroundResource(if (repeat.contains(REPEAT_THURSDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatFriday.setTextColorRes(if (repeat.contains(REPEAT_FRIDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatFriday.setBackgroundResource(if (repeat.contains(REPEAT_FRIDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatSaturday.setTextColorRes(if (repeat.contains(REPEAT_SATURDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatSaturday.setBackgroundResource(if (repeat.contains(REPEAT_SATURDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            binding.tvRepeatSunday.setTextColorRes(if (repeat.contains(REPEAT_SUNDAY)) R.color.text_color else R.color.text_color_secondary)
            binding.tvRepeatSunday.setBackgroundResource(if (repeat.contains(REPEAT_SUNDAY)) R.drawable.bg_selected_repeat else R.color.transparent_color)

            if (it.notifyTime != 0L) {
                binding.tvNotifyMeTime.visibility = View.VISIBLE
                binding.tvNotifyMeTime.text = SimpleDateFormat("HH:mm").format(notifyTime)
                binding.btnNotifyMe.setImageResource(R.drawable.ic_cancel_notify)
            }
        }
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            if (binding.etRoutineName.text.toString().isEmpty()) {
                Toast.makeText(
                    this@CustomRoutineActivity,
                    "Please enter your routine name",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (controller.getListData().isEmpty()) {
                Toast.makeText(
                    this@CustomRoutineActivity,
                    "Please create at least 1 routine task",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isUpdate) updateRoutine()
                else createNewRoutine()
            }
        }

        binding.btnAddNewTask.setOnClickListener {
            if (binding.etTaskName.text.isEmpty()) {
                Toast.makeText(
                    this@CustomRoutineActivity,
                    "Please enter task name",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                controller.getListData().add(0, RoutineTask(autoGenId, binding.etTaskName.text.toString()))
                binding.rvRoutineTasks.requestModelBuild()
                while (setIdNotUse.contains(autoGenId)) autoGenId++
            }
        }

        binding.tvRepeatMonday.setOnClickListener {
            if (repeat.contains(REPEAT_MONDAY)) {
                repeat = repeat.replace(REPEAT_MONDAY, "")
                binding.tvRepeatMonday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatMonday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_MONDAY
                binding.tvRepeatMonday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatMonday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatTuesday.setOnClickListener {
            if (repeat.contains(REPEAT_TUESDAY)) {
                repeat = repeat.replace(REPEAT_TUESDAY, "")
                binding.tvRepeatTuesday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatTuesday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_TUESDAY
                binding.tvRepeatTuesday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatTuesday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatWednesday.setOnClickListener {
            if (repeat.contains(REPEAT_WEDNESDAY)) {
                repeat = repeat.replace(REPEAT_WEDNESDAY, "")
                binding.tvRepeatWednesday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatWednesday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_WEDNESDAY
                binding.tvRepeatWednesday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatWednesday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatThursday.setOnClickListener {
            if (repeat.contains(REPEAT_THURSDAY)) {
                repeat = repeat.replace(REPEAT_THURSDAY, "")
                binding.tvRepeatThursday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatThursday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_THURSDAY
                binding.tvRepeatThursday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatThursday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatFriday.setOnClickListener {
            if (repeat.contains(REPEAT_FRIDAY)) {
                repeat = repeat.replace(REPEAT_FRIDAY, "")
                binding.tvRepeatFriday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatFriday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_FRIDAY
                binding.tvRepeatFriday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatFriday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatSaturday.setOnClickListener {
            if (repeat.contains(REPEAT_SATURDAY)) {
                repeat = repeat.replace(REPEAT_SATURDAY, "")
                binding.tvRepeatSaturday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatSaturday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_SATURDAY
                binding.tvRepeatSaturday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatSaturday.setTextColorRes(R.color.text_color)
            }
        }

        binding.tvRepeatSunday.setOnClickListener {
            if (repeat.contains(REPEAT_SUNDAY)) {
                repeat = repeat.replace(REPEAT_SUNDAY, "")
                binding.tvRepeatSunday.setBackgroundResource(R.color.transparent_color)
                binding.tvRepeatSunday.setTextColorRes(R.color.text_color_secondary)
            } else {
                repeat += REPEAT_SUNDAY
                binding.tvRepeatSunday.setBackgroundResource(R.drawable.bg_selected_repeat)
                binding.tvRepeatSunday.setTextColorRes(R.color.text_color)
            }
        }

        binding.btnNotifyMe.setOnClickListener {
            if (notifyTime == 0L) {
                onHoursClicked()
            } else {
                notifyTime = 0
                binding.tvNotifyMeTime.visibility = View.GONE
                binding.btnNotifyMe.setImageResource(R.drawable.ic_notify_me)
            }
        }

        binding.tvNotifyMeTime.setOnClickListener {
            onHoursClicked()
        }
    }

    private fun updateRoutine() {
        curRoutine?.let {
            it.routineTasks.clear()
            it.routineTasks.addAll(controller.getListData())
            it.repeat = repeat
            it.notifyTime = notifyTime
            it.routineName = binding.etRoutineName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                MyDatabase.getInstance(this@CustomRoutineActivity).routineDao().updateRoutine(
                    it
                )
                withContext(Dispatchers.Main) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun onHoursClicked() {
        val timePicker = TimePickerDialog(
            this,
            R.style.TimePickerTheme,
            { _, hourOfDay, minute -> updateHours(hourOfDay, minute) },
            myCalendar[Calendar.HOUR_OF_DAY],
            myCalendar[Calendar.MINUTE],
            true
        )
        timePicker.setOnDismissListener { }
        timePicker.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateHours(hourOfDay: Int, minute: Int) {
        myCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        myCalendar[Calendar.MINUTE] = minute
        val hour = SimpleDateFormat("HH:mm").format(myCalendar.time)
        notifyTime = myCalendar.timeInMillis
        binding.tvNotifyMeTime.visibility = View.VISIBLE
        binding.tvNotifyMeTime.text = hour
        binding.btnNotifyMe.setImageResource(R.drawable.ic_cancel_notify)
    }


    private fun createNewRoutine() {
        CoroutineScope(Dispatchers.IO).launch {
            val now = LocalDate.now()
            MyDatabase.getInstance(this@CustomRoutineActivity).routineDao().insertRoutine(
                Routine(
                    routineName = binding.etRoutineName.text.toString(),
                    routineGoal = "",
                    repeat = repeat,
                    notifyTime = 0,
                    routineTasks = controller.getListData(),
                    startDate = Timeline(
                        0,
                        LocalDateUtil.fromDateToString(now),
                        CalendarUtil.getCurrentHour(),
                        CalendarUtil.getCurrentMinute(),
                        CalendarUtil.getCurrentSecond()
                    ),
                    completeDates = ArrayList<Timeline>()
                )
            )
            withContext(Dispatchers.Main) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupView() {
        binding.rvRoutineTasks.setControllerAndBuildModels(controller)

        EpoxyTouchHelper.initDragging(controller)
            .withRecyclerView(binding.rvRoutineTasks)
            .forGrid()
            .forAllModels()
            .andCallbacks(object : EpoxyTouchHelper.DragCallbacks<EpoxyModel<*>>() {
                override fun onModelMoved(
                    fromPosition: Int,
                    toPosition: Int,
                    modelBeingMoved: EpoxyModel<*>?,
                    itemView: View?
                ) {
                    val removed = controller.getListData().removeAt(fromPosition)
                    controller.getListData().add(toPosition, removed)
                    controller.requestModelBuild()
                }
            })
    }

    private fun initData() {
        controller.getListData().add(RoutineTask(autoGenId, "Go for a walk"))
        autoGenId++
        controller.getListData().add(RoutineTask(autoGenId, "Drink water"))
        autoGenId++
        controller.getListData().add(RoutineTask(autoGenId, "Take a cold shower"))
        autoGenId++
    }
}