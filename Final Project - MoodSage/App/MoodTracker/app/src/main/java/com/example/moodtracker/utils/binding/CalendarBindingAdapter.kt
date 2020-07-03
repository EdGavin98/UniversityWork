package com.example.moodtracker.utils.binding

import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.ui.home.calendar.CalendarDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


@BindingAdapter("moods")
fun MaterialCalendarView.bindMoods(moods: List<Mood>?) {
    moods?.let { it ->
        removeDecorators()
        addDecorators(
            CalendarDecorator(it, 1, 2, Color.parseColor("#FF0000")),
            CalendarDecorator(it, 3, 4, Color.parseColor("#FF8800")),
            CalendarDecorator(it, 5, 6, Color.parseColor("#FFFF00")),
            CalendarDecorator(it, 7, 8, Color.parseColor("#88FF00")),
            CalendarDecorator(it, 9, 10, Color.parseColor("#00FF00"))
        )
    }
}