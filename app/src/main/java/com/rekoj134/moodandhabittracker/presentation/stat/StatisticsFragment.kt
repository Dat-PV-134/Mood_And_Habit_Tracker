package com.rekoj134.moodandhabittracker.presentation.stat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.constant.EMOTION_BAD
import com.rekoj134.moodandhabittracker.constant.EMOTION_GOOD
import com.rekoj134.moodandhabittracker.constant.EMOTION_NORMAL
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.databinding.FragmentStatisticsBinding
import com.rekoj134.moodandhabittracker.db.MyDatabase
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.model.FocusTime
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.presentation.settings.SettingsActivity
import com.rekoj134.moodandhabittracker.util.LocalDateUtil
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.roundToInt

class StatisticsFragment : BaseFragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataRoutine()
        getDataMoodVer2()
        setupChartFocus()
        getDataJournal()

        binding?.btnSettings?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    private fun getDataRoutine() {
        CoroutineScope(Dispatchers.IO).launch {
            val allRoutine = MyDatabase.getInstance(requireContext()).routineDao().getAllRoutine()

            val currentDate = LocalDate.now()
            val firstDayOfMonth = currentDate.withDayOfMonth(1)

            val values = ArrayList<Entry>()

            var count = 0
            Log.e("ANCUTKO", firstDayOfMonth.toString())
            var currentDateIterator = firstDayOfMonth
            while (currentDateIterator.isBefore(currentDate) || currentDateIterator.isEqual(currentDate)) {
                Log.e("ANCUTKO", currentDateIterator.toString())
                allRoutine.forEach {
                    var tempDate = LocalDate.now().plus(1, ChronoUnit.DAYS)
                    it.completeDates.forEach { timeline ->
                        Log.e("ANCUTKO", tempDate.toString())
                        if (tempDate != LocalDateUtil.fromStringToDate(timeline.date)) {
                            tempDate = LocalDateUtil.fromStringToDate(timeline.date)
                            if (tempDate == currentDateIterator) {
                                count++
                            }
                        }
                    }
                }
                Log.e("ANCUTKO", count.toString())
                values.add(Entry(currentDateIterator.dayOfMonth.toFloat(), count.toFloat()))
                count = 0
                currentDateIterator = currentDateIterator.plus(1, ChronoUnit.DAYS)
            }

            val set1 = LineDataSet(values, "DataSet 1")

            set1.lineWidth = 1.75f
            set1.circleRadius = 4f
            set1.circleHoleRadius = 2f
            set1.color = Color.parseColor("#94CADB")
            set1.setCircleColor(Color.parseColor("#94CADB"))
            set1.highLightColor = Color.parseColor("#94CADB")
            set1.setDrawValues(false)
            set1.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart)
            set1.setDrawFilled(true)

            withContext(Dispatchers.Main) {
                setupChart(binding?.myChart!!, LineData(set1), Color.TRANSPARENT)
            }
        }
    }

    private fun getDataMoodVer2() {
        CoroutineScope(Dispatchers.IO).launch {
            val allMood = MyDatabase.getInstance(requireContext()).moodDao().getAllMoodSorted()

            val currentDate = LocalDate.now()
            val firstDayOfMonth = currentDate.withDayOfMonth(1)

            var countTerrible = 0
            var countBad = 0
            var countNormal = 0
            var countGood = 0
            var countPerfect = 0
            var currentDateIterator = firstDayOfMonth

            val values = ArrayList<Entry>()
            val values2 = ArrayList<Entry>()
            val values3 = ArrayList<Entry>()
            val values4 = ArrayList<Entry>()
            val values5 = ArrayList<Entry>()

            while (currentDateIterator.isBefore(currentDate) || currentDateIterator.isEqual(currentDate)) {
                allMood.forEach {
                    if (LocalDateUtil.fromStringToDate(it.date.date) == currentDateIterator) {
                        if (it.mood == EMOTION_TERRIBLE) countTerrible++
                        if (it.mood == EMOTION_BAD) countBad++
                        if (it.mood == EMOTION_GOOD) countNormal++
                        if (it.mood == EMOTION_GOOD) countGood++
                        if (it.mood == EMOTION_PERFECT) countPerfect++
                    }
                }

                values5.add(Entry(currentDateIterator.dayOfMonth.toFloat(), countTerrible.toFloat()))
                values4.add(Entry(currentDateIterator.dayOfMonth.toFloat(), countBad.toFloat()))
                values3.add(Entry(currentDateIterator.dayOfMonth.toFloat(), countNormal.toFloat()))
                values2.add(Entry(currentDateIterator.dayOfMonth.toFloat(), countGood.toFloat()))
                values.add(Entry(currentDateIterator.dayOfMonth.toFloat(), countPerfect.toFloat()))
                countTerrible = 0
                countBad = 0
                countNormal = 0
                countGood = 0
                countPerfect = 0
                currentDateIterator = currentDateIterator.plus(1, ChronoUnit.DAYS)
            }

            val set5 = LineDataSet(values5, getString(R.string.terrible))

            set5.lineWidth = 1.75f
            set5.circleRadius = 4f
            set5.circleHoleRadius = 2f
            set5.color = Color.parseColor("#FA9999")
            set5.setCircleColor(Color.parseColor("#FA9999"))
            set5.highLightColor = Color.parseColor("#FA9999")
            set5.setDrawValues(false)
            set5.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_terrible)
            set5.setDrawFilled(true)

            val set4 = LineDataSet(values4, getString(R.string.bad))

            set4.lineWidth = 1.75f
            set4.circleRadius = 4f
            set4.circleHoleRadius = 2f
            set4.color = Color.parseColor("#F5BB90")
            set4.setCircleColor(Color.parseColor("#F5BB90"))
            set4.highLightColor = Color.parseColor("#F5BB90")
            set4.setDrawValues(false)
            set4.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_bad)
            set4.setDrawFilled(true)

            val set3 = LineDataSet(values3, getString(R.string.normal))

            set3.lineWidth = 1.75f
            set3.circleRadius = 4f
            set3.circleHoleRadius = 2f
            set3.color = Color.parseColor("#98C7AB")
            set3.setCircleColor(Color.parseColor("#98C7AB"))
            set3.highLightColor = Color.parseColor("#98C7AB")
            set3.setDrawValues(false)
            set3.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_normal)
            set3.setDrawFilled(true)

            val set2 = LineDataSet(values2, getString(R.string.good))

            set2.lineWidth = 1.75f
            set2.circleRadius = 4f
            set2.circleHoleRadius = 2f
            set2.color = Color.parseColor("#8EC7BD")
            set2.setCircleColor(Color.parseColor("#8EC7BD"))
            set2.highLightColor = Color.parseColor("#8EC7BD")
            set2.setDrawValues(false)
            set2.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_good)
            set2.setDrawFilled(true)

            val set1 = LineDataSet(values, getString(R.string.perfect))

            set1.lineWidth = 1.75f
            set1.circleRadius = 4f
            set1.circleHoleRadius = 2f
            set1.color = Color.parseColor("#94CADB")
            set1.setCircleColor(Color.parseColor("#94CADB"))
            set1.highLightColor = Color.parseColor("#94CADB")
            set1.setDrawValues(false)
            set1.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart)
            set1.setDrawFilled(true)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set5)
            dataSets.add(set4)
            dataSets.add(set3)
            dataSets.add(set2)
            dataSets.add(set1)
            val lineData = LineData(dataSets)

            withContext(Dispatchers.Main) {
                setupChart(binding?.myChartMood!!, lineData, Color.TRANSPARENT, isEnableLegend = true)
            }
        }
    }

    private fun setupChart(chart: LineChart, data: LineData, color: Int, isEnableLegend: Boolean = false) {
//        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

        chart.description.isEnabled = false

        chart.setGridBackgroundColor(Color.parseColor("#FFFFFF"))
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(false)
        chart.setBackgroundColor(color)

        chart.data = data

        val l = chart.legend
        l.isEnabled = isEnableLegend

        chart.setExtraOffsets(0f,0f,0f,15f)

        chart.axisRight.setDrawGridLines(false);
        chart.axisLeft.setDrawGridLines(false);
        chart.xAxis.setDrawGridLines(false);

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisLeft.isEnabled = false

        val calendar = Calendar.getInstance()
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val monthDays = mutableListOf<String>()
        for (day in 1..currentDayOfMonth) {
            monthDays.add(day.toString())
        }

        // Convert to Array if needed
        val monthDaysArray = monthDays.toTypedArray()

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(monthDaysArray)

        val right = chart.axisRight
        right.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.roundToInt().toString()
            }
        }
        right.isGranularityEnabled = true;

        chart.setVisibleXRange(7f, 7f)
        chart.moveViewToX(currentDayOfMonth.toFloat())

        // animate calls invalidate()...
        chart.animateX(1200)
    }

    private fun setupChartFocus() {
        CoroutineScope(Dispatchers.IO).launch {
            val entries = ArrayList<PieEntry>()
            val dataSetColors = HashSet<Int>()
            val dataSetData = HashSet<String>()
            val focusList = MyDatabase.getInstance(requireContext()).focusDao().getAllFocus()

            var totalTime = 0L
            focusList.forEach {
                Log.e("DAWFwafw", it.toString())
                totalTime += it.focusedTime.time
                dataSetColors.add(Color.parseColor(it.color.replace("#4D", "#")))
                dataSetData.add(it.label)
            }

            dataSetData.forEach {
                var value = 0f
                focusList.forEach { focusItem ->
                    if (focusItem.label == it) value += focusItem.focusedTime.time
                }
                entries.add(PieEntry(value/totalTime * 100, it))
                Log.e("DAWFwafw", (value).toString())
                Log.e("DAWFwafw", (totalTime).toString())
                Log.e("DAWFwafw", (value/totalTime * 100).toString())
            }

            val dataSet = PieDataSet(entries, "")
            Log.e("DAWFwafw", dataSetColors.toString())
            dataSet.colors = dataSetColors.toList()
            dataSet.sliceSpace = 2.5f

            val data = PieData(dataSet)
            data.setDrawValues(false)
            data.setValueFormatter(PercentFormatter(binding?.myChartFocus))
            data.setValueTextSize(0f)
            data.setValueTextColor(Color.BLACK)

            withContext(Dispatchers.Main) {
                binding?.apply {
                    myChartFocus.description.isEnabled = false
                    myChartFocus.data = data
                    myChartFocus.invalidate()
                    myChartFocus.animateY(1200, Easing.EaseInOutQuad)
                }
            }
        }
    }

    private fun getDataJournal() {
        CoroutineScope(Dispatchers.IO).launch {
            val allJournal = MyDatabase.getInstance(requireContext()).journalDao().getAllJournalSorted()

            val currentDate = LocalDate.now()
            val firstDayOfMonth = currentDate.withDayOfMonth(1)

            val values = ArrayList<Entry>()

            var count = 0
            var currentDateIterator = firstDayOfMonth
            while (currentDateIterator.isBefore(currentDate) || currentDateIterator.isEqual(currentDate)) {
                allJournal.forEach {
                    if (LocalDateUtil.fromStringToDate(it.date.date) == currentDateIterator) {
                        count++
                    }
                }
                values.add(Entry(currentDateIterator.dayOfMonth.toFloat(), count.toFloat()))
                count = 0
                currentDateIterator = currentDateIterator.plus(1, ChronoUnit.DAYS)
            }

            val set1 = LineDataSet(values, "DataSet 1")

            set1.lineWidth = 1.75f
            set1.circleRadius = 4f
            set1.circleHoleRadius = 2f
            set1.color = Color.parseColor("#94CADB")
            set1.setCircleColor(Color.parseColor("#94CADB"))
            set1.highLightColor = Color.parseColor("#94CADB")
            set1.setDrawValues(false)
            set1.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart)
            set1.setDrawFilled(true)

            withContext(Dispatchers.Main) {
                binding?.myChartJournal?.let {
                    setupChart(it, LineData(set1), Color.TRANSPARENT)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}