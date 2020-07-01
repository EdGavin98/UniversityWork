package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.network.ApiCallState
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.getAverageRatingByDay
import com.example.moodtracker.utils.mappers.convertToMoodEntity
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class MoodRepositoryImpl @Inject
constructor(
    private val remote: MoodNetwork,
    private val local: MoodLocal,
    private val sp: SharedPreferencesManager
) : MoodRepository {


    override suspend fun getAllMoodRatings(): Resource<List<DateRating>> {
        return Resource.success(
            data = local.getAllMoodRatings()
        )
    }

    override suspend fun getLatestMoods(): Resource<Unit> {
        return remote.getMoods("Bearer ${sp.getToken()}").run {
            when (state) {
                ApiCallState.SUCCESS -> {
                    local.addMoods(
                        moods = data!!.map { dto ->
                            dto.convertToMoodEntity()
                        }
                    )
                    Resource.success(Unit)
                }

                ApiCallState.ERROR -> Resource.failure("Server error while getting moods", null)

                ApiCallState.NETWORK_ERROR -> Resource.failure(
                    "Network error while getting moods",
                    null
                )

            }
        }
    }

    override suspend fun deleteMood(date: LocalDateTime): Resource<Unit> {
        return remote.deleteMood("Bearer ${sp.getToken()}", date).run {
            when(state) {
                ApiCallState.SUCCESS -> {
                    local.deleteMoodByDate(date)
                    Resource.success(Unit)
                }
                ApiCallState.ERROR -> Resource.failure("Server error: unable to delete", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network error: no connection", null)
            }
        }
    }

    override suspend fun getAverageMoodRatingByDay(): Resource<FloatArray> {
        return try {
            local.getAllMoodRatings().let {
                Resource.success(
                    data = getAverageRatingByDay(it)
                )
            }
        } catch (e: Exception) {
            Resource.failure("An Error Occurred", null)
        }

    }

    override suspend fun getMoodByDate(date: LocalDateTime): Resource<Mood> {
        return local.getMoodByDate(date).let { mood ->
            if (mood != null) {
                Resource.success(mood)
            } else {
                Resource.failure("No mood exists on this date", null)
            }
        }
    }

    override suspend fun getMoodsFromMonth(date: LocalDate): Resource<List<Mood>> {
        val end = LocalDate.of(date.year, date.monthValue, date.lengthOfMonth())
        return local.getMoodsFromRange(date.atStartOfDay(), end.atTime(23, 59)).let { moods ->
            if (moods.isNotEmpty()) {
                Resource.success(moods)
            } else {
                Resource.failure("No moods found for this month", moods)
            }
        }
    }

    override suspend fun syncMood(date: LocalDateTime): Resource<Unit> {
        val mood = local.getMoodByDate(date)
        return if (mood != null) {
            remote.addMood(mood, "Bearer ${sp.getToken()}").run {
                when (state) {
                    ApiCallState.SUCCESS -> {
                        local.updateMoodUploadStatus(date, true)
                        Resource.success(Unit) //Return that it was saved successfully
                    }
                    ApiCallState.ERROR -> Resource.failure(
                        "Server error",
                        null
                    ) //Return that a server error caused failure
                    ApiCallState.NETWORK_ERROR -> Resource.failure(
                        "Network error",
                        null
                    )   //Return that a network error caused failure
                }
            }
        } else {
            Resource.failure("No Mood", null)    //Mood may have been removed before sync occurred
        }
    }

    override suspend fun addMood(
        date: LocalDateTime,
        rating: Int,
        comment: String,
        isPrivate : Boolean
    ): Resource<Unit> {
        val mood = Mood(
            userId = sp.getUser(),
            date = date,
            rating = rating,
            comment = comment,
            isPrivate = isPrivate
        )

        return if (validateMood(mood)) {
            local.addMood(mood)
            Resource.success(Unit)
        } else {
            Resource.failure("Error adding mood. Please check details and try again", null)
        }

    }

    ////////////////////////// Helper Functions ///////////////////////////////
    /**
     * Helper function to check that a moods details are correct and that it doesn't already exist.
     *
     * @param mood The mood that needs to be checked
     * @return Boolean - Whether or not the mood is valid
     */
    private suspend fun validateMood(mood: Mood): Boolean {
        if (mood.comment.length > 250) {
            return false
        }

        if (mood.rating <= 0 || mood.rating > 10) {
            return false
        }

        if (local.getMoodByDate(mood.date) != null) {
            return false
        }

        return true
    }

}