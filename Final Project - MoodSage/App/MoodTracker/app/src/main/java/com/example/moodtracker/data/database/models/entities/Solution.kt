package com.example.moodtracker.data.database.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "solutions",
    foreignKeys = [
        ForeignKey(
            entity = Worry::class,
            parentColumns = ["date"],
            childColumns = ["worry_date"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Solution(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "worry_date")
    var worryDate: LocalDateTime,

    @ColumnInfo(name = "time_logged")
    var timeLogged: LocalDateTime,

    var description: String,

    var advantages: String,

    var disadvantages: String,

    var uploaded: Boolean = false

)