package com.example.moodtracker.repository.account

import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.data.network.models.UserRegisterDto
import com.example.moodtracker.repository.Resource

interface AccountRepository {

    /***
     * Log the user into the application
     * @param email - Email to authenticate with
     * @param password - Password to authenticate with
     * @return Resource with the status of the operation
     */
    suspend fun login(email: String, password: String): Resource<Unit>

    /***
     * Log the user out of the application, removing all data
     * @return Resource with the status of the operation
     */
    suspend fun logout(): Resource<Unit>

    /***
     * See whether a user is logged in to the application or not
     * @return Resource with the status of the operation and boolean representing login status
     */
    fun getLoginStatus(): Resource<Boolean>

    /***
     * Create an account for the application
     * @param userDto - Object with user details to create account with
     * @return Resource with the status of the operation
     */
    suspend fun createAccount(userDto: UserRegisterDto): Resource<Unit>

    /***
     * Update an accounts details
     * @param user - New details to update with
     * @return Resource with the status of the operation
     */
    suspend fun updateAccount(user: User): Resource<Unit>

    /***
     * Delete the current account
     * @return Resource with the status of the operation
     */
    suspend fun deleteAccount(): Resource<Unit>

    /***
     * Get the details of the currently logged in account
     * @return Resource with the status of the operation and user details if successful
     */
    suspend fun getCurrentAccount(): Resource<User>

    /***
     * Get all of an accounts links
     * @return @return Resource with the status of the operation and list of links if successful
     */
    suspend fun getLinks(): Resource<List<LinkDto>>

    /***
     * Accept an account link
     * @param id - ID of the account to accept
     * @return Resource with the status of the operation
     */
    suspend fun acceptLink(id: String): Resource<Unit>

    /***
     * remove an account link
     * @param id - ID of the account to remove
     * @return Resource with the status of the operation
     */
    suspend fun removeLink(id: String): Resource<Unit>

}