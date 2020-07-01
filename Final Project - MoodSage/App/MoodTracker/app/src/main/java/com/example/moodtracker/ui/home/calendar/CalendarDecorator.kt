package com.example.moodtracker.ui.home.calendar

import com.example.moodtracker.data.database.models.entities.Mood
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CalendarDecorator(
    private var moods: List<Mood>,
    private val thresholdMin: Int,
    private val thresholdMax: Int,
    private val colour: Int
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        for (mood in moods) {
            val moodDay = CalendarDay.from(mood.date.toLocalDate())
            if (moodDay == day && mood.rating >= thresholdMin && mood.rating <= thresholdMax) return true
        }
        return false
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(20.0f, colour))
    }

}