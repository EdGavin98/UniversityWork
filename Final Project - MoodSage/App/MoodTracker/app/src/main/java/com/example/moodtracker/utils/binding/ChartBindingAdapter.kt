package com.example.moodtracker.utils.binding

import androidx.databinding.BindingAdapter
import com.example.moodtracker.utils.graphformatters.DayValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData

@BindingAdapter("groupedBarChartData")
fun BarChart.addBarDataset(barChartData: BarData?) {

    val groupSpace = 0.1f
    val barSpace =
        0.05f        //(0.4 + 0.05) * 2 + 0.1 = 1, granularity is 1, so group width should be as well
    val barWidth = 0.4f

    barChartData?.let { barData ->
        barData.isHighlightEnabled = false
        data = barData

        with(xAxis) {
            granularity = 1f
            labelCount = 7
            position = XAxis.XAxisPosition.BOTTOM
            axisMinimum = 0f
            valueFormatter = DayValueFormatter()
            xAxis.setCenterAxisLabels(true)
            setDrawGridLines(false)
        }
        with(axisLeft) {
            setDrawGridLines(false)
            spaceBottom = 0f
            axisMaximum = 10f
            axisMinimum = 0f
            setDrawZeroLine(false)
        }
        axisRight.isEnabled = false
        description.isEnabled = false
        setScaleEnabled(false)

        barData.barWidth = barWidth
        groupBars(0f, groupSpace, barSpace)
        invalidate()
    }
}

@BindingAdapter("lineChartData")
fun LineChart.addLineDataset(lineChartData: LineData?) {
    lineChartData?.let { lineData ->
        lineData.isHighlightEnabled = false
        data = lineData

        xAxis.isEnabled = false
        with(axisLeft) {
            spaceBottom = 0f
            granularity = 1f
            setDrawZeroLine(false)
            setDrawGridLines(false)
            axisMaximum = 10f
            axisMinimum = 0f
        }
        axisRight.isEnabled = false

        description.isEnabled = false
        isDragEnabled = false
        setScaleEnabled(false)

        invalidate()
    }
}

@BindingAdapter("pieChartData")
fun PieChart.addPieDataset(pieChartData: PieData?) {
    pieChartData?.let { pieData ->
        pieData.isHighlightEnabled = false
        data = pieData
        description.isEnabled = false
        legend.isEnabled = false
        invalidate()
    }
}