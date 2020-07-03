package com.example.moodtracker.data.database.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    var id: String,

    var forename: String,

    var surname: String,

    var email: String,

    var moodTarget: Int = 0,

    var worryTarget: Int = 0

)
