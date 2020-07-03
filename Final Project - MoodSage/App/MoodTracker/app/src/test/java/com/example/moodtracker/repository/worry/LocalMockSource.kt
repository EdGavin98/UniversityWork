package com.example.moodtracker.repository.worry

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import org.threeten.bp.LocalDateTime

class LocalMockSource : WorryLocal {

    private var worries = mutableListOf(
        Worry(
            user = "id",
            type = "Current",
            severity = 4,
            description = "Test",
            date = LocalDateTime.of(2020, 4, 1, 0,0)
        ),
        Worry(
            user = "id",
            type = "Hypothetical",
            severity = 6,
            description = "Test2",
            date = LocalDateTime.of(2020, 4, 1, 0,1)
        ),
        Worry(
            user = "id",
            type = "Current",
            severity = 5,
            description = "Test3",
            date = LocalDateTime.of(2020, 4, 2, 0,0)
        )
    )

    override fun getAllWorries(): LiveData<List<Worry>> {
        return liveData {
            worries
        }
    }

    override suspend fun addWorry(worry: Worry) {
        worries.add(worry)
    }

    override suspend fun addWorry(worry: Worry, solutions: List<Solution>) {
        worries.add(worry)
    }

    override suspend fun addWorries(worries: List<Worry>) {
        this.worries.addAll(worries)
    }

    override suspend fun getAllWorrySeverities(): List<DateRating> {
        val ratings = mutableListOf<DateRating>()
        worries.forEach {
            ratings.add( DateRating(
                date = it.date,
                value = it.severity
            ))
        }
        return ratings
    }

    override suspend fun getCurrentAndHypotheticalCount(): List<WorryTypeCount> {
        var currentCount = 0
        var hypotheticalCount = 0

        worries.forEach {
            if (it.type == "Current") {
                currentCount++
            } else {
                hypotheticalCount++
            }
        }

        return listOf(
            WorryTypeCount(
                type = "Current",
                count = currentCount
            ),
            WorryTypeCount(
                type = "Hypothetical",
                count = hypotheticalCount
            )
        )
    }

    override suspend fun getWorryByDate(date: LocalDateTime): Worry? {
        return worries.find {
            it.date == date
        }
    }

    override suspend fun updateWorryUploadStatus(date: LocalDateTime, isUploaded: Boolean) {
        worries.find {it.date == date}?.uploaded = isUploaded
    }

    override suspend fun deleteWorryByDate(date: LocalDateTime) {
        worries.find {
            it.date == date
        }.also {
            worries.remove(it)
        }

    }

}