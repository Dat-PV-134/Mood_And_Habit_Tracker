package com.rekoj134.moodandhabittracker.presentation.stat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import com.rekoj134.moodandhabittracker.constant.EMOTION_PERFECT
import com.rekoj134.moodandhabittracker.constant.EMOTION_TERRIBLE
import com.rekoj134.moodandhabittracker.databinding.FragmentStatisticsBinding
import com.rekoj134.moodandhabittracker.model.Focus
import com.rekoj134.moodandhabittracker.model.FocusTime
import com.rekoj134.moodandhabittracker.model.Timeline
import com.rekoj134.moodandhabittracker.presentation.settings.SettingsActivity
import com.rekoj134.moodandhabittracker.util.ModelConverterUtil
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

        val data = getData(10, 4f)
        val dataMood = getDataMood(7, 5f)
        val dataJournal = getDataJournal(7, 5f)

        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(binding!!.myChart, data, Color.parseColor("#F8F8F8"))
        setupChartMoods(binding!!.myChartMood, dataMood, Color.parseColor("#F8F8F8"))
        setupChartFocus(binding!!.myChartFocus, Color.parseColor("#F8F8F8"))
        setupChartJournal(binding!!.myChartJournal, dataJournal, Color.parseColor("#F8F8F8"))

        binding?.btnSettings?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    private fun getData(count: Int, range: Float): LineData {
        val values = ArrayList<Entry>()

        for (i in 0 until 7) {
            val value = (Math.random() * range.toInt()).toFloat()
            values.add(Entry(i.toFloat(), value))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")

        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);
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

        return LineData(set1)
    }

    private fun setupChart(chart: LineChart, data: LineData, color: Int) {
        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

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
        l.isEnabled = false

        chart.axisRight.setDrawGridLines(false);
        chart.axisLeft.setDrawGridLines(false);
        chart.xAxis.setDrawGridLines(false);

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisLeft.isEnabled = false

        val weekdays = arrayOf(
            "M",
            "T",
            "W",
            "T",
            "F",
            "S",
            "S"
        )

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)

        val right = chart.axisRight
        right.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.roundToInt().toString()
            }
        }
        right.isGranularityEnabled = true;

        // animate calls invalidate()...
        chart.animateX(1200)
    }

    private fun getDataMood(count: Int, range: Float): LineData {
        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        val values3 = ArrayList<Entry>()
        val values4 = ArrayList<Entry>()
        val values5 = ArrayList<Entry>()

        val value = arrayListOf(1f, 0f, 0f, 0f, 0f, 2f, 0f)
        for (i in 0 until 7) {
            values5.add(Entry(i.toFloat(), value[i]))
        }

        // create a dataset and give it a type
        val set5 = LineDataSet(values5, "DataSet 1")

        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);
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

        val value2 = arrayListOf(0f, 0f, 0f, 1f, 0f, 1f, 0f)
        for (i in 0 until 7) {
            values4.add(Entry(i.toFloat(), value2[i]))
        }

        val set4 = LineDataSet(values4, "DataSet 2")

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

        val value3 = arrayListOf(0f, 0f, 1f, 0f, 2f, 0f, 0f)
        for (i in 0 until 7) {
            values3.add(Entry(i.toFloat(), value3[i]))
        }

        val set3 = LineDataSet(values3, "DataSet 2")

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

        val value4 = arrayListOf(0f, 0f, 0f, 2f, 0f, 3f, 1f)
        for (i in 0 until 7) {
            values2.add(Entry(i.toFloat(), value4[i]))
        }

        val set2 = LineDataSet(values2, "DataSet 2")

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

        val value5 = arrayListOf(0f, 1f, 0f, 0f, 1f, 0f, 3f)
        for (i in 0 until 7) {
            values.add(Entry(i.toFloat(), value5[i]))
        }

        val set1 = LineDataSet(values, "DataSet 2")

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

        return lineData
    }

    private fun setupChartMoods(chart: LineChart, data: LineData, color: Int) {
        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

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
        l.isEnabled = false

        chart.axisRight.setDrawGridLines(false);
        chart.axisLeft.setDrawGridLines(false);
        chart.xAxis.setDrawGridLines(false);

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisLeft.isEnabled = false

        val right = chart.axisRight
        right.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.roundToInt().toString()
            }
        }
        right.isGranularityEnabled = true;

        val weekdays = arrayOf(
            "M",
            "T",
            "W",
            "T",
            "F",
            "S",
            "S"
        )

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)

        // animate calls invalidate()...
        chart.animateX(1200)
    }

    private fun getDataFocus(count: Int, range: Float): LineData {
        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 3
            values.add(Entry(i.toFloat(), value))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")

        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);
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

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 3
            values2.add(Entry(i.toFloat(), value))
        }

        val set2 = LineDataSet(values2, "DataSet 2")

        set2.lineWidth = 1.75f
        set2.circleRadius = 4f
        set2.circleHoleRadius = 2f
        set2.color = Color.parseColor("#FA9999")
        set2.setCircleColor(Color.parseColor("#FA9999"))
        set2.highLightColor = Color.parseColor("#FA9999")
        set2.setDrawValues(false)
        set2.fillDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_terrible)
        set2.setDrawFilled(true)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        val lineData = LineData(dataSets)

        return lineData
    }

    private fun setupChartFocus(chart: PieChart, color: Int) {
        val entries = ArrayList<PieEntry>()
        val dataSetColors = ArrayList<Int>()
        val dataSetData = ArrayList<Long>()
        val reportItemList = arrayListOf<Focus>(
            Focus(
                0,
                "Work",
                FocusTime(
                    0,
                    1500000,
                    ModelConverterUtil.fromTimelineToString(Timeline(0, "15-05-2024", 15, 30, 0))
                ),
                EMOTION_TERRIBLE,
                Timeline(0, "15-05-2024", 15, 30, 0)
            ),
            Focus(
                1,
                "Study",
                FocusTime(
                    0,
                    3000000,
                    ModelConverterUtil.fromTimelineToString(Timeline(0, "15-05-2024", 15, 30, 0))
                ),
                EMOTION_BAD,
                Timeline(0, "15-05-2024", 15, 30, 0)
            ),
            Focus(
                2,
                "Relax",
                FocusTime(
                    0,
                    3000000,
                    ModelConverterUtil.fromTimelineToString(Timeline(0, "16-05-2024", 15, 30, 0))
                ),
                EMOTION_GOOD,
                Timeline(0, "15-05-2024", 15, 30, 0)
            ),
            Focus(
                3,
                "Dating",
                FocusTime(
                    0,
                    1500000,
                    ModelConverterUtil.fromTimelineToString(Timeline(0, "17-05-2024", 15, 30, 0))
                ),
                EMOTION_PERFECT,
                Timeline(0, "17-05-2024", 15, 30, 0)
            )
        )

        var totalTime = 0L
        reportItemList.forEach {
            totalTime += it.focusedTime.time
            dataSetData.add(it.focusedTime.time)
        }

        reportItemList.forEach {
            entries.add(PieEntry(25f, it.label))
        }

        dataSetColors.add(Color.parseColor("#FA9999"))
        dataSetColors.add(Color.parseColor("#98C7AB"))
        dataSetColors.add(Color.parseColor("#8EC7BD"))
        dataSetColors.add(Color.parseColor("#94CADB"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = dataSetColors
        dataSet.sliceSpace = 2.5f

        val data = PieData(dataSet)
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter(binding?.myChartFocus))
        data.setValueTextSize(0f)
        data.setValueTextColor(Color.BLACK)
        binding?.apply {
            myChartFocus.description.isEnabled = false
            myChartFocus.data = data
            myChartFocus.invalidate()
            myChartFocus.animateY(1200, Easing.EaseInOutQuad)
        }

    }

    private fun getDataJournal(count: Int, range: Float): LineData {
        val values = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat()
            values.add(Entry(i.toFloat(), value))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")

        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);
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

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 3
            values2.add(Entry(i.toFloat(), value))
        }

        val set2 = LineDataSet(values2, "DataSet 2")

        set2.lineWidth = 1.75f
        set2.circleRadius = 4f
        set2.circleHoleRadius = 2f
        set2.color = Color.parseColor("#FA9999")
        set2.setCircleColor(Color.parseColor("#FA9999"))
        set2.highLightColor = Color.parseColor("#FA9999")
        set2.setDrawValues(false)
        set2.fillDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart_terrible)
        set2.setDrawFilled(true)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val lineData = LineData(dataSets)

        return lineData
    }

    private fun setupChartJournal(chart: LineChart, data: LineData, color: Int) {
        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

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
        l.isEnabled = false

        chart.axisRight.setDrawGridLines(false);
        chart.axisLeft.setDrawGridLines(false);
        chart.xAxis.setDrawGridLines(false);

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisLeft.isEnabled = false

        val right = chart.axisRight
        right.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.roundToInt().toString()
            }
        }
        right.isGranularityEnabled = true;

        val weekdays = arrayOf(
            "M",
            "T",
            "W",
            "T",
            "F",
            "S",
            "S"
        )

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)

        // animate calls invalidate()...
        chart.animateX(2500)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}