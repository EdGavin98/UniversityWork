package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.repository.Resource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface MoodRepository {

    /***
     * Add a new mood
     * @param date - The date of the mood to be added
     * @param rating - The rating of the mood
     * @param comment - The users comments on the mood
     * @param isPrivate - The privacy setting of the mood
     * @return Resource object containing the status of the operation
     */
    suspend fun addMood(date: LocalDateTime, rating: Int, comment: String, isPrivate : Boolean): Resource<Unit>

    /***
     * Synchronise a mood with the network
     * @param date - Date of the mood to synchronise
     * @return Resource object containing the status of the operation
     */
    suspend fun syncMood(date: LocalDateTime): Resource<Unit>

    /***
     * Get moods from a specific month
     * @param date - LocalDateTime for the first day of that month
     * @return Resource with status success and the list if there are any, else resource with failure
     */
    suspend fun getMoodsFromMonth(date: LocalDate): Resource<List<Mood>>

    /***
     * Get a mood from a specific date
     * @param date - Date of the mood to find
     * @return Resource with status success and the mood if it is exists, else resource with failure
     */
    suspend fun getMoodByDate(date: LocalDateTime): Resource<Mood>

    /***
     * Get the average mood rating for each day of the week
     * @return Resource with status of the operation and the average for each day in an array
     */
    suspend fun getAverageMoodRatingByDay(): Resource<FloatArray>

    /***
     * Get dates and ratings for each mood
     * @return Resource with status of the operation and a list of mood dates and ratings
     */
    suspend fun getAllMoodRatings(): Resource<List<DateRating>>

    /***
     * Get latest moods of user, synchronising with the server
     * @return Resource with the status of the operation
     */
    suspend fun getLatestMoods(): Resource<Unit>

    /***
     * Delete a mood
     * @param date - Date of the mood to be deleted
     * @return Resource object containing the status of the operation
     */
    suspend fun deleteMood(date: LocalDateTime): Resource<Unit>

}