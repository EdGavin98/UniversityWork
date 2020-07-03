package com.example.moodtracker.repository.worry

import androidx.lifecycle.LiveData
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import com.example.moodtracker.repository.Resource
import org.threeten.bp.LocalDateTime

interface WorryRepository {

    /***
     * Add a new worry
     * @param date - Date of the worry
     * @param severity - Severity of the worry
     * @param description - Description of the worry
     * @param type - Type of the worry
     * @param private - The privacy status of the worry
     * @return Resource object with the status of the operation
     */
    suspend fun addNewWorry(
        date: LocalDateTime,
        severity: Int,
        description: String,
        type: String,
        private: Boolean = false
    ): Resource<Unit>

    /***
     * Synchronise a worry with the server
     * @param date - Date of the worry to synchronise
     * @return Resource object with the status of the operation
     */
    suspend fun syncWorry(date: LocalDateTime): Resource<Unit>

    /***
     * Get all of the users worries
     * @return Resource object with the operation status, and LiveData of the worries
     */
    fun getAllWorries(): Resource<LiveData<List<Worry>>>

    /***
     * Get the latest worries of the user, synchronising with the server
     * @return Resource containing the status of the operation
     */
    suspend fun getLatestWorries(): Resource<Unit>

    /***
     *  Get a worry for a specific date
     *  @param date - date of the worry to retrieve
     *  @return Resource object containing the status of the operation and a worry, returns failure if no worry exists
     */
    suspend fun getWorryByDate(date: LocalDateTime): Resource<Worry>

    /***
     *  Get the average worry severity for each day of the week
     *  @return Resource containing the status of the operation and array with value for each day
     */
    suspend fun getWorrySeverityAverageByDay(): Resource<FloatArray>

    /***
     *  Get the date and severity of each worry
     *  @return Resource containing the status of the operation and a list of all ratings and their dates
     */
    suspend fun getAllWorrySeverities(): Resource<List<DateRating>>

    /***
     * Get the count of worries for each type
     *  @return Resource containing the status of the operation and a list of each types count
     */
    suspend fun getCurrentAndHypotheticalCount(): Resource<List<WorryTypeCount>>

    /***
     * Delete a specific worry
     * @param date - Date of the worry to be deleted
     * @return Resource containing the status of the operation
     */
    suspend fun deleteWorry(date: LocalDateTime): Resource<Unit>



}