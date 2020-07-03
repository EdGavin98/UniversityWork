package com.example.moodtracker.repository.worry

import androidx.lifecycle.LiveData
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.database.models.returnobjects.WorryTypeCount
import org.threeten.bp.LocalDateTime

interface WorryLocal {

    /***
     *  Get all the users current worries
     * @return LiveData, all worries
     */
    fun getAllWorries(): LiveData<List<Worry>>

    /***
     * Add a new worry to the local data source
     * @param worry - Worry entity to be added
     * @return Nothing
     */
    suspend fun addWorry(worry: Worry)

    /***
     * Add a new worry, with solutions to the local data source
     * @param worry - worry entity to be added
     * @param solutions - solutions to be added alongside
     * @return Nothing
     */
    suspend fun addWorry(worry: Worry, solutions: List<Solution>)

    /***
     *  Add a list of worries to the local data source
     *  @param  worries - List of worries to be added to the datasource
     *  @return Nothing
     */
    suspend fun addWorries(worries: List<Worry>)

    /***
     * Get just the worry dates and severities of all worries
     * @return List of Dates and Ratings for all worries
     */
    suspend fun getAllWorrySeverities(): List<DateRating>

    /***
     *  Get the counts of each worry type
     *  @return List of all counts for each type
     */
    suspend fun getCurrentAndHypotheticalCount(): List<WorryTypeCount>

    /***
     * Get a worry from the local data source for each date
     * @param date - date to search for a worry on
     * @return If found, the worry from that date. Else, null
     */
    suspend fun getWorryByDate(date: LocalDateTime): Worry?

    /***
     * Update the upload status of a specific worry
     * @param date - The date of the worry to update
     * @param isUploaded - The new upload status
     * @return Nothing
     */
    suspend fun updateWorryUploadStatus(date: LocalDateTime, isUploaded: Boolean)

    /***
     *  Delete a worry on a specific date
     *  @param date - Date of the worry to be deleted
     */
    suspend fun deleteWorryByDate(date: LocalDateTime)

}