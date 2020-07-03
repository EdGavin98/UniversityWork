package com.example.moodtracker.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class MoshiDateConverter {

    @ToJson
    fun dateToJson(date: LocalDateTime): String {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date)
    }

    @FromJson
    fun jsonToDate(date: String): LocalDateTime {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .parse(date, LocalDateTime.FROM)
    }

}