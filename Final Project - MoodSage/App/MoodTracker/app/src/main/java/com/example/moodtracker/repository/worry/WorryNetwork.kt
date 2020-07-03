package com.example.moodtracker.repository.worry

import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.WorryDto
import org.threeten.bp.LocalDateTime

interface WorryNetwork {

    /***
     * Send a new worry to the remote data source
     * @param token - The users API token
     * @param worry - the worry to be sent to the server
     * @return ApiCall object with the request status
     */
    suspend fun addWorry(worry: Worry, token: String): ApiCall<Unit>

    /***
     *  Get all of a users worries from the remote data source
     *  @param token - The users API token
     *  @return ApiCall object with the request status and list of Worries
     */
    suspend fun getWorries(token: String): ApiCall<List<WorryDto>>

    /***
     *  Delete a worry from the remote data source
     *  @param token - The users API token
     *  @param date - The date of the worry to be delete
     *  @return ApiCall object with the request status
     */
    suspend fun deleteWorry(token: String, date: LocalDateTime): ApiCall<Unit>

}