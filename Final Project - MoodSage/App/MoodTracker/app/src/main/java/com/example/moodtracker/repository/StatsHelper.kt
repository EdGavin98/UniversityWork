package com.example.moodtracker.repository

import com.example.moodtracker.data.database.models.returnobjects.DateRating
import org.threeten.bp.DayOfWeek

/***
 * Package level function to calculate the daily average of a list of DateRatings
 * @param ratings - list of date ratings
 * @return FloatArray of ratings 0 = Monday, 6 = Sunday
 */
fun getAverageRatingByDay(ratings: List<DateRating>): FloatArray {
    val monday = mutableListOf<Int>()
    val tuesday = mutableListOf<Int>()
    val wednesday = mutableListOf<Int>()
    val thursday = mutableListOf<Int>()
    val friday = mutableListOf<Int>()
    val saturday = mutableListOf<Int>()
    val sunday = mutableListOf<Int>()

    for (moodRating in ratings) {
        when (moodRating.date.dayOfWeek) {
            DayOfWeek.MONDAY -> monday.add(moodRating.value)
            DayOfWeek.TUESDAY -> tuesday.add(moodRating.value)
            DayOfWeek.WEDNESDAY -> wednesday.add(moodRating.value)
            DayOfWeek.THURSDAY -> thursday.add(moodRating.value)
            DayOfWeek.FRIDAY -> friday.add(moodRating.value)
            DayOfWeek.SATURDAY -> saturday.add(moodRating.value)
            DayOfWeek.SUNDAY -> sunday.add(moodRating.value)
            null -> {
            } //Do nothing
        }
    }

    return floatArrayOf(
        monday.average().toFloat(),
        tuesday.average().toFloat(),
        wednesday.average().toFloat(),
        thursday.average().toFloat(),
        friday.average().toFloat(),
        saturday.average().toFloat(),
        sunday.average().toFloat()
    )
}