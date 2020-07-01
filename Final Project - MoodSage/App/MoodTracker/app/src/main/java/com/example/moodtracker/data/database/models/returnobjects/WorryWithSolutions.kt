package com.example.moodtracker.data.database.models.returnobjects

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry

data class WorryWithSolutions(
    @Embedded
    val worry: Worry? = null,

    @Relation(
        parentColumn = "date",
        entityColumn = "worry_date",
        entity = Solution::class
    )
    var solutions: List<Solution> = emptyList()
)