package com.example.moodtracker.utils

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime

class RoomDateConverter {

    @TypeConverter
    fun stringToDate(date: String?): LocalDateTime? {
        return LocalDateTime.parse(date)
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date.toString()
    }

}