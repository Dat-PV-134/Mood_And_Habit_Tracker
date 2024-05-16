package com.rekoj134.moodandhabittracker.presentation.stat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rekoj134.moodandhabittracker.R
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.databinding.FragmentStatisticsBinding


class StatisticsFragment: BaseFragment() {
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

        val data = getData(36, 100f)

        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(binding!!.myChart, data, Color.parseColor("#FFFFFF"))
    }

    private fun getData(count: Int, range: Float): LineData {
        val values = ArrayList<Entry>()

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 3
            values.add(Entry(i.toFloat(), value))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")

        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);
        set1.lineWidth = 1.75f
        set1.circleRadius = 5f
        set1.circleHoleRadius = 2.5f
        set1.color = Color.parseColor("#94CADB")
        set1.setCircleColor(Color.parseColor("#94CADB"))
        set1.highLightColor = Color.parseColor("#94CADB")
        set1.setDrawValues(false)
        set1.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_line_chart)
        set1.setDrawFilled(true)

        return LineData(set1)
    }

    private fun setupChart(chart: LineChart, data: LineData, color: Int) {
        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

        // no description text
        chart.description.isEnabled = false

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false)

        //        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setBackgroundColor(color)

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        // add data
        chart.data = data

        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = false

        chart.axisLeft.isEnabled = false
        chart.axisLeft.spaceTop = 40f
        chart.axisLeft.spaceBottom = 40f
        chart.axisRight.isEnabled = false

        chart.xAxis.isEnabled = false

        // animate calls invalidate()...
        chart.animateX(2500)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}