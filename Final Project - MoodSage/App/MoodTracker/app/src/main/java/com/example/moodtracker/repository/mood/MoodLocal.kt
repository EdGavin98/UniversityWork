package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import org.threeten.bp.LocalDateTime

interface MoodLocal {

    /***
     * Add a mood to the local data source
     * @param mood - The mood to be added
     */
    suspend fun addMood(mood: Mood)

    /***
     * Add multiple moods to the local data source
     * @param moods - The list of moods to be added
     */
    suspend fun addMoods(moods: List<Mood>)

    /***
     * Get all moods from the local data source
     * @return List of all moods
     */
    suspend fun getAllMoods(): List<Mood>

    /***
     * Get a mood on a specific date
     * @param date - Date of the mood to find
     * @return Mood from that date, null if none exist
     */
    suspend fun getMoodByDate(date: LocalDateTime): Mood?

    /***
     * Get moods from a specific date range
     * @param monthStart - Start of the range
     * @param monthEnd - End of the range
     * @return List of moods from within that range
     */
    suspend fun getMoodsFromRange(monthStart: LocalDateTime, monthEnd: LocalDateTime): List<Mood>

    /***
     *  Get the ratings and dates for each mood
     *  @return List of all dates and ratings
     */
    suspend fun getAllMoodRatings(): List<DateRating>

    /***
     * Update the upload status of a mood
     * @param date - Date of the mood to update
     * @param isUploaded - New upload status
     * @return Nothing
     */
    suspend fun updateMoodUploadStatus(date: LocalDateTime, isUploaded: Boolean)

    /***
     * Delete mood on a specific date
     * @param date - Date of the mood to delete
     * @return Nothing
     */
    suspend fun deleteMoodByDate(date: LocalDateTime)

}