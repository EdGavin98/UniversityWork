package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.MoodDto
import org.threeten.bp.LocalDateTime

interface MoodNetwork {

    /***
     * Add a mood to the remote data source
     * @param mood - The mood to be added
     * @param token - The users API token
     * @return ApiCall object with the status of the operation
     */
    suspend fun addMood(mood: Mood, token: String): ApiCall<Unit>

    /***
     * Get all moods from the remote data source
     * @param token - The users API token
     * @return ApiCall with the status of the request and list of moods if successful
     */
    suspend fun getMoods(token: String): ApiCall<List<MoodDto>>

    /***
     * Delete a mood from the remote data source
     * @param token - The users API token
     * @param date - Date of the mood to delete
     * @return ApiCall object with the status of the operation
     */
    suspend fun deleteMood(token: String, date: LocalDateTime): ApiCall<Unit>

}