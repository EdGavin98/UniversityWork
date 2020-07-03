package com.example.moodtracker.ui.home.dashboard

import android.graphics.Color
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import com.github.mikephil.charting.data.*
import org.threeten.bp.ZoneOffset

fun buildMoodWorryByDayRatingDataset(
    moodAverages: FloatArray,
    worryAverages: FloatArray,
    colourMain: String,
    colourSecondary: String
): BarData {
    val moodEntries = ArrayList<BarEntry>()
    val worryEntries = ArrayList<BarEntry>()
    for (index in 0..6) {
        moodEntries.add(BarEntry(index.toFloat(), moodAverages[index]))
        worryEntries.add(BarEntry(index.toFloat(), worryAverages[index]))
    }
    val moodDataset = BarDataSet(moodEntries, "Mood Averages").apply {
        color = Color.parseColor(colourMain)
    }
    val worryDataset = BarDataSet(worryEntries, "Worry Averages").apply {
        color = Color.parseColor(colourSecondary)

    }

    return BarData(moodDataset, worryDataset)
}

fun buildMoodWorryOverTimeDataset(
    moodsOverTime: List<DateRating>,
    worriesOverTime: List<DateRating>,
    colourMain: String,
    colourSecondary: String
): LineData {
    val moodEntries = ArrayList<Entry>()
    val worryEntries = ArrayList<Entry>()

    for (index in moodsOverTime.indices) {
        val rating = moodsOverTime[index].value.toFloat()
        val date = moodsOverTime[index].date.toEpochSecond(ZoneOffset.UTC).toFloat()
        moodEntries.add(Entry(date, rating))
    }
    for (index in worriesOverTime.indices) {
        val rating = worriesOverTime[index].value.toFloat()
        val date = worriesOverTime[index].date.toEpochSecond(ZoneOffset.UTC).toFloat()
        worryEntries.add(Entry(date, rating))
    }

    worryEntries.sortBy { entry -> entry.x }
    moodEntries.sortBy { entry -> entry.x }

    val moodDataset = LineDataSet(moodEntries, "Moods").apply {
        setDrawCircles(false)
        setDrawValues(false)
        color = Color.parseColor(colourMain)
        lineWidth = 2f
    }
    val worryDataset = LineDataSet(worryEntries, "Worries").apply {
        setDrawCircles(false)
        setDrawValues(false)
        color = Color.parseColor(colourSecondary)
        lineWidth = 2f
    }
    return LineData(moodDataset, worryDataset)
}


fun buildCurrentHypotheticalCountDataset(
    counts: List<WorryTypeCount>,
    colourMain: String,
    colourSecondary: String
): PieData {
    val countEntries = ArrayList<PieEntry>()
    for (type in counts) {
        countEntries.add(PieEntry(type.count.toFloat(), type.type))
    }

    val chartColours = listOf(Color.parseColor(colourMain), Color.parseColor(colourSecondary))

    val countDataset = PieDataSet(countEntries, "Worries By Type").apply {
        colors = chartColours
        valueTextSize += 3f
    }


    return PieData(countDataset)

}

fun buildSolvedWorriesCountDataset(
    with: Int,
    without: Int,
    colourMain: String,
    colourSecondary: String
): PieData {
    val countEntries = listOf(
        PieEntry(with.toFloat(), "With"),
        PieEntry(without.toFloat(), "Without")
    )

    val chartColours = listOf(Color.parseColor(colourMain), Color.parseColor(colourSecondary))

    val countDataset = PieDataSet(countEntries, "Worries By Type").apply {
        colors = chartColours
        valueTextSize += 3f
    }


    return PieData(countDataset)

}