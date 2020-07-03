package com.example.moodtracker.repository.worry

import androidx.lifecycle.LiveData
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import com.example.moodtracker.data.network.ApiCallState
import com.example.moodtracker.data.network.models.WorryDto
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.getAverageRatingByDay
import com.example.moodtracker.utils.mappers.convertToSolutionEntity
import com.example.moodtracker.utils.mappers.convertToWorryEntity
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class WorryRepositoryImpl @Inject constructor(
    private val local: WorryLocal,
    private val remote: WorryNetwork,
    private val sp: SharedPreferencesManager
) : WorryRepository {

    override suspend fun getCurrentAndHypotheticalCount(): Resource<List<WorryTypeCount>> {
        return local.getCurrentAndHypotheticalCount().let { counts ->
            if (counts.isNotEmpty()) {
                Resource.success(counts)
            } else {
                Resource.failure("No data available for this graph", emptyList())
            }
        }
    }

    override suspend fun deleteWorry(date: LocalDateTime): Resource<Unit> {
        return remote.deleteWorry("Bearer ${sp.getToken()}", date).run {
            when(state) {
                ApiCallState.SUCCESS -> {
                    local.deleteWorryByDate(date)
                    Resource.success(Unit)
                }
                ApiCallState.ERROR -> Resource.failure("Server Error: unable to delete", null)
                ApiCallState.NETWORK_ERROR -> Resource.failure("Network Error: no connection", null)
            }
        }
    }

    override suspend fun syncWorry(date: LocalDateTime): Resource<Unit> {
        val worry = local.getWorryByDate(date)
        return if (worry != null) {
            remote.addWorry(worry, "Bearer ${sp.getToken()}").run {
                when (state) {
                    ApiCallState.SUCCESS -> {
                        local.updateWorryUploadStatus(date, true)
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
            Resource.failure("No Worry", null)    //Worry may have been removed before sync occurred
        }
    }

    override suspend fun getAllWorrySeverities(): Resource<List<DateRating>> {
        return Resource.success(
            data = local.getAllWorrySeverities()
        )
    }


    override suspend fun getWorrySeverityAverageByDay(): Resource<FloatArray> {
        return local.getAllWorrySeverities().let { ratings ->
            if (ratings.isNotEmpty()) {
                Resource.success(
                    data = getAverageRatingByDay(ratings)
                )
            } else {
                Resource.failure(
                    "No worry severity data available",
                    floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
                )
            }
        }
    }


    override fun getAllWorries(): Resource<LiveData<List<Worry>>> {
        return Resource.success(
            data = local.getAllWorries()
        )
    }

    override suspend fun getWorryByDate(date: LocalDateTime): Resource<Worry> {
        return local.getWorryByDate(date).let { worry ->
            if (worry != null) {
                Resource.success(worry)
            } else {
                Resource.failure("No worry exists on this date", null)
            }
        }
    }

    override suspend fun getLatestWorries(): Resource<Unit> {
        return remote.getWorries("Bearer ${sp.getToken()}").run {
            when (state) {
                ApiCallState.SUCCESS -> {
                    convertAndAddToDatabase(data!!)
                    Resource.success(Unit)
                }
                ApiCallState.ERROR -> Resource.failure(
                    "Server error while retrieving worries",
                    null
                )
                ApiCallState.NETWORK_ERROR -> Resource.failure(
                    "No network connection available",
                    null
                )
            }
        }
    }


    override suspend fun addNewWorry(
        date: LocalDateTime,
        severity: Int,
        description: String,
        type: String,
        private: Boolean
    ): Resource<Unit> {

        val worry = Worry(
            user = sp.getUser(),
            date = date,
            severity = severity,
            description = description,
            type = type,
            isPrivate = private
        )

        return if (validateWorry(worry)) {
            local.addWorry(worry)
            Resource.success(Unit)
        } else {
            Resource.failure("Error adding worry: Please check details and try again", null)
        }

    }

    //////////////////////////////////// Helpers /////////////////////////////////////////

    /***
     * Helper function to add a worry and it's solutions to the database when retrieved from server
     */
    private suspend fun convertAndAddToDatabase(worries: List<WorryDto>) {
        for (worry in worries) {
            local.addWorry(
                worry = worry.convertToWorryEntity(),
                solutions = worry.solutions.map { it.convertToSolutionEntity(worry.date) }
            )
        }
    }

    /***
     * Helper function to validate a worries details and make sure it doesn't exist
     * @param worry - Worry to validate
     * @return Boolean for is the worry valid
     */
    private suspend fun validateWorry(worry: Worry): Boolean {
        if (local.getWorryByDate(worry.date) != null) {
            return false
        }

        if (worry.description.isBlank() || worry.description.length > 500) {
            return false
        }

        if (!arrayOf("Current", "Hypothetical").contains(worry.type)) {
            return false
        }

        if (worry.severity <= 0 || worry.severity > 10) {
            return false
        }

        return true
    }


}