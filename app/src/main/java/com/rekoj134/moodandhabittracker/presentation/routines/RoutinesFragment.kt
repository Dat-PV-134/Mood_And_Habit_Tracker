package com.rekoj134.moodandhabittracker.presentation.routines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.constant.EXTRA_ROUTINE
import com.rekoj134.moodandhabittracker.constant.STREAK_DAY_1
import com.rekoj134.moodandhabittracker.constant.STREAK_DAY_2
import com.rekoj134.moodandhabittracker.constant.STREAK_DAY_3
import com.rekoj134.moodandhabittracker.constant.STREAK_DAY_4
import com.rekoj134.moodandhabittracker.constant.STREAK_DONE
import com.rekoj134.moodandhabittracker.constant.STREAK_NONE
import com.rekoj134.moodandhabittracker.constant.STREAK_NOT_DONE
import com.rekoj134.moodandhabittracker.constant.TYPE_DELETE
import com.rekoj134.moodandhabittracker.constant.TYPE_EDIT
import com.rekoj134.moodandhabittracker.constant.TYPE_HISTORY
import com.rekoj134.moodandhabittracker.databinding.FragmentRoutinesBinding
import com.rekoj134.moodandhabittracker.databinding.PopupRoutineBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.itemLoadingFull
import com.rekoj134.moodandhabittracker.itemRoutine
import com.rekoj134.moodandhabittracker.itemRoutineTask
import com.rekoj134.moodandhabittracker.model.Routine
import com.rekoj134.moodandhabittracker.model.RoutineTask
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.preference.MyPreferences
import com.rekoj134.moodandhabittracker.util.CalendarUtil
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


class RoutinesFragment : BaseFragment() {
    private var _binding: FragmentRoutinesBinding? = null
    private val binding get() = _binding

    private val listRoutines = HashSet<Routine>()
    private val setRoutineCompleteToday = HashSet<Int>()
    private val listOfExpand = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutinesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupView()
        handleEvent()
    }

    private fun observerData() {
        setOfTaskCompleteToday.observe(requireActivity()) {
            Log.e("AWDAWDWFFW", "COME HERE")
            binding?.rvRoutines?.requestModelBuild()
        }

        currentChangeRoutine.observe(requireActivity()) {
            listRoutines.forEach {
                if (it.id == currentChangeRoutine.value) {
                    updateRoutines(it)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun updateRoutines(routine: Routine) {
        var isCompleteToday = true
        val routineTemp = Routine(
            routine.id,
            routine.routineName,
            routine.routineGoal,
            routine.repeat,
            routine.notifyTime,
            routine.routineTasks,
            routine.startDate,
            routine.completeDates
        )

        Log.e("ANCUTKO", setOfTaskCompleteToday.value.toString())
        routineTemp.routineTasks.forEach { task ->
            val pattern = "parent_" + routineTemp.id + "child_" + task.id
            Log.e("ANCUTKO", pattern.toString())
            if (!setOfTaskCompleteToday.value!!.contains(pattern)) isCompleteToday = false
        }
        Log.e("ANCUTKO", "Today " + isCompleteToday.toString())

        if (isCompleteToday) {
            setRoutineCompleteToday.remove(routine.id)
            setRoutineCompleteToday.add(routine.id)
            Log.e("ANCUTKO", "Today " + setRoutineCompleteToday.toString())
            routineTemp.completeDates.add(
                Timeline(
                    routineTemp.completeDates.size + 1,
                    LocalDateUtil.fromDateToString(LocalDate.now()),
                    CalendarUtil.getCurrentHour(),
                    CalendarUtil.getCurrentMinute(),
                    CalendarUtil.getCurrentSecond()
                )
            )
        } else {
            setRoutineCompleteToday.remove(routine.id)
            routineTemp.completeDates.forEach {
                if (LocalDateUtil.fromStringToDate(it.date) == LocalDate.now()) routineTemp.completeDates.remove(
                    it
                )
            }
        }

        binding?.rvRoutines?.requestModelBuild()

        CoroutineScope(Dispatchers.IO).launch {
            MyPreferences.write(
                MyPreferences.PREF_COMPLETE_TASKS_TODAY,
                ModelConverterUtil.fromPatternToString(setOfTaskCompleteToday.value!!.toList())
            )
            MyDatabase.getInstance(requireContext()).routineDao().updateRoutine(routineTemp)
        }
    }

    private fun handleEvent() {
        binding?.btnAdd?.setOnClickListener {
            customRoutineLauncher.launch(Intent(requireContext(), CustomRoutineActivity::class.java))
        }
    }

    private val customRoutineLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            listRoutines.clear()
            binding?.rvRoutines?.requestModelBuild()
            initData()
        }
    }

    private fun setupView() {
        binding?.rvRoutines?.withModels {
            if (listRoutines.isEmpty()) {
                itemLoadingFull {
                    id("loading")
                }
            }
            listRoutines.forEach {
                itemRoutine {
                    id(it.id)
                    parentId(it.id)
                    routineName(it.routineName)
                    routineTasks(it.routineTasks)
                    progress(getRoutineProgress(it.id, it.routineTasks))
                    progressRatio(getRoutineProgressRatio(it.id, it.routineTasks))
                    statusStreakDay1(
                        getStatusStreak(
                            STREAK_DAY_1,
                            LocalDateUtil.fromStringToDate(it.startDate.date),
                            getNearestCompleteDate(it.completeDates)
                        )
                    )
                    statusStreakDay2(
                        getStatusStreak(
                            STREAK_DAY_2,
                            LocalDateUtil.fromStringToDate(it.startDate.date),
                            getNearestCompleteDate(it.completeDates)
                        )
                    )
                    statusStreakDay3(
                        getStatusStreak(
                            STREAK_DAY_3,
                            LocalDateUtil.fromStringToDate(it.startDate.date),
                            getNearestCompleteDate(it.completeDates)
                        )
                    )
                    statusStreakDay4(
                        getStatusStreak(
                            STREAK_DAY_4,
                            LocalDateUtil.fromStringToDate(it.startDate.date),
                            getNearestCompleteDate(it.completeDates)
                        )
                    )
                    statusStreakDay5(
                        getStatusStreak(
                            it
                        )
                    )
                    onClickExpand { _ ->
                        if (listOfExpand.contains(it.id)) listOfExpand.remove(it.id)
                        else listOfExpand.add(it.id)
                        binding?.rvRoutines?.requestModelBuild()
                    }
                    onClickMore { view ->
                        showPopupMore(view) { type ->
                            doActionPopup(type, it)
                        }
                    }
                    onClickStreak { _ ->
                        goToDetail(it)
                    }
                    isExpand(listOfExpand.contains(it.id))
                }
            }
        }
    }

    private fun doActionPopup(type: Int, item: Routine) {
        when (type) {
            TYPE_EDIT -> {
                val intent = Intent(requireContext(), CustomRoutineActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable(EXTRA_ROUTINE, item)
                intent.putExtras(bundle)
                customRoutineLauncher.launch(intent)
            }

            TYPE_HISTORY -> {
                goToDetail(item)
            }

            TYPE_DELETE -> {
                listRoutines.remove(item)
                binding?.rvRoutines?.requestModelBuild()
                CoroutineScope(Dispatchers.IO).launch {
                    MyDatabase.getInstance(requireContext()).routineDao().deleteRoutine(item)
                }
            }
        }
    }

    private fun showPopupMore(view: View, onDone: (Int) -> Unit) {
        val popupInflater =
            requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = PopupRoutineBinding.inflate(popupInflater)

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

        popupBind.btnHistory.setOnClickListener {
            onDone(TYPE_HISTORY)
            popupWindow.dismiss()
        }

        popupBind.btnDelete.setOnClickListener {
            onDone(TYPE_DELETE)
            popupWindow.dismiss()
        }

        popupWindow.isClippingEnabled = false
        popupWindow.showAsDropDown(view, -300, -40)
    }

    private fun goToDetail(item: Routine) {
        val intent = Intent(requireContext(), RoutineDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_ROUTINE, item)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getNearestCompleteDate(listTimeLine: List<Timeline>): List<LocalDate> {
        val sortedList = listTimeLine.sortedByDescending { LocalDateUtil.fromStringToDate(it.date) }
        val listNearestDate = sortedList.take(5).map { LocalDateUtil.fromStringToDate(it.date) }
        Log.e("ANCUTKO", listNearestDate.toString())
        return listNearestDate
    }

    private fun getStatusStreak(
        streakDay: Int,
        startDate: LocalDate,
        completeDates: List<LocalDate>
    ): Int {
        when (streakDay) {
            STREAK_DAY_1 -> {
                val dayOfStreak1 = LocalDate.now().minusDays(4)
                if (startDate > dayOfStreak1) return STREAK_NONE
                return if (completeDates.contains(dayOfStreak1)) STREAK_DONE
                else STREAK_NOT_DONE
            }

            STREAK_DAY_2 -> {
                val dayOfStreak2 = LocalDate.now().minusDays(3)
                if (startDate > dayOfStreak2) return STREAK_NONE
                return if (completeDates.contains(dayOfStreak2)) STREAK_DONE
                else STREAK_NOT_DONE
            }

            STREAK_DAY_3 -> {
                val dayOfStreak3 = LocalDate.now().minusDays(2)
                if (startDate > dayOfStreak3) return STREAK_NONE
                return if (completeDates.contains(dayOfStreak3)) STREAK_DONE
                else STREAK_NOT_DONE
            }

            STREAK_DAY_4 -> {
                val dayOfStreak4 = LocalDate.now().minusDays(1)
                Log.e("ANCUTKO", "Trek 4: " + dayOfStreak4.toString())
                if (startDate > dayOfStreak4) return STREAK_NONE
                return if (completeDates.contains(dayOfStreak4)) STREAK_DONE
                else STREAK_NOT_DONE
            }

            else -> return STREAK_NONE
        }
    }

    private fun getStatusStreak(routine: Routine): Int {
        return if (setRoutineCompleteToday.contains(routine.id)) STREAK_DONE
        else STREAK_NONE
    }

    private fun getRoutineProgress(parentId: Int, routineTasks: List<RoutineTask>): Float {
        var completeTask = 0f
        routineTasks.forEach {
            if (setOfTaskCompleteToday.value!!.contains("parent_" + parentId + "child_" + it.id)) completeTask++
        }
        return (completeTask / routineTasks.size) * 100
    }

    private fun getRoutineProgressRatio(parentId: Int, routineTasks: List<RoutineTask>): String {
        var completeTask = 0
        routineTasks.forEach {
            if (setOfTaskCompleteToday.value!!.contains("parent_" + parentId + "child_" + it.id)) completeTask++
        }
        return completeTask.toString() + "/" + routineTasks.size
    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {
            listRoutines.clear()
            listRoutines.addAll(
                MyDatabase.getInstance(requireContext()).routineDao().getAllRoutine()
            )
            withContext(Dispatchers.Main) {
                setOfTaskCompleteToday.value = HashSet<String>()
                setOfTaskCompleteToday.value!!.addAll(
                    ModelConverterUtil.fromStringToPattern(
                        MyPreferences.read(MyPreferences.PREF_COMPLETE_TASKS_TODAY, "")!!
                    )
                )
                listRoutines.forEach { routine ->
                    var isCompleteToday = true
                    routine.routineTasks.forEach { task ->
                        if (!setOfTaskCompleteToday.value?.contains("parent_" + routine.id + "child_" + task.id)!!) {
                            isCompleteToday = false
                        }
                    }
                    if (isCompleteToday) setRoutineCompleteToday.add(routine.id)
                }
                observerData()
            }
        }
    }

    override fun onDestroy() {
        Log.e("ANCUTKO", "Destroyed")
        super.onDestroy()
        _binding = null
    }

    companion object {
        val setOfTaskCompleteToday = MutableLiveData<HashSet<String>>()
        var currentChangeRoutine = MutableLiveData<Int>()
    }
}

@BindingAdapter("bind:status_streak")
fun setStatusStreak(view: ImageView, streakType: Int) {
    when (streakType) {
        STREAK_DONE -> view.setImageResource(R.drawable.ic_done)
        STREAK_NOT_DONE -> view.setImageResource(R.drawable.ic_not_done)
        STREAK_NONE -> view.setImageResource(R.drawable.bg_circle_no_data)
    }
}

@BindingAdapter("bind:progress")
fun setProgress(view: CircularProgressBar, progress: Float) {
    view.progress = progress
}

@BindingAdapter("bind:visibility")
fun setVisibility(view: EpoxyRecyclerView, isExpand: Boolean) {
    if (isExpand) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("bind:data", "bind:parent_id")
fun setData(view: Carousel, data: List<RoutineTask>, parentId: Int) {
    view.withModels {
        data.forEach {
            itemRoutineTask {
                id("parent_" + parentId + "child_" + it.id)
                task(it.taskName)
                isComplete(RoutinesFragment.setOfTaskCompleteToday.value!!.contains("parent_" + parentId + "child_" + it.id))
                onClickItem { _ ->
                    val tempSet =
                        RoutinesFragment.setOfTaskCompleteToday.value!!.clone() as HashSet<String>
                    if (!tempSet.remove("parent_" + parentId + "child_" + it.id)) tempSet.add(
                        "parent_" + parentId + "child_" + it.id
                    )
                    RoutinesFragment.setOfTaskCompleteToday.value = tempSet
                    RoutinesFragment.currentChangeRoutine.value = parentId
                    this@withModels.requestModelBuild()
                }
            }
        }
    }
}

@BindingAdapter("bind:task_status")
fun setTaskStatus(view: ImageView, isComplete: Boolean) {
    Log.e("ANCUTKO", isComplete.toString())
    view.setImageResource(if (isComplete) R.drawable.ic_done else R.drawable.bg_circle_no_data)
}

@BindingAdapter("bind:task_underline")
fun setTaskUnderline(view: TextView, isUnderline: Boolean) {
    if (isUnderline) view.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    else view.paintFlags = 0
}