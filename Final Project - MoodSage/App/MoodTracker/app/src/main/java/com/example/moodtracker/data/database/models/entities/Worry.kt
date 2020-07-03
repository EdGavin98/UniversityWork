package com.example.moodtracker.data.database.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(
    tableName = "worries",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Worry(

    @PrimaryKey
    var date: LocalDateTime,

    @ColumnInfo(name = "user_id")
    var user: String,

    var severity: Int,

    var description: String,

    var type: String,

    var isPrivate: Boolean = false,

    var uploaded: Boolean = false

)
