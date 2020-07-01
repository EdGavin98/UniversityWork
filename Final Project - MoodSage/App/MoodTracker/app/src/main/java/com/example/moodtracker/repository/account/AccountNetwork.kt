package com.example.moodtracker.repository.account

import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.data.network.models.TokenDto
import com.example.moodtracker.data.network.models.UserDto
import com.example.moodtracker.data.network.models.UserRegisterDto

interface AccountNetwork {

    /***
     * Send an authentication request to the server
     * @param email - Email to authenticate with
     * @param password - Password to authenticate with
     * @return ApiCall with success status and token if request is valid, error status type if not
     */
    suspend fun login(email: String, password: String): ApiCall<TokenDto>

    /***
     * Add a new account to the remote data source
     * @param userDto - User account to be added
     * @return ApiCall object with status of request
     */
    suspend fun createAccount(userDto: UserRegisterDto): ApiCall<Unit>

    /***
     * Update the details of an account on the remote data source
     * @param userDto - Details of the account to update
     * @param token - The users API token
     * @return ApiCall object with status of the request
     */
    suspend fun updateAccount(user: UserDto, token: String): ApiCall<Unit>

    /***
     * Retrieve account details from the remote data source
     * @param token - The users API token
     * @return ApiCall with the status of the request and user details if successful
     */
    suspend fun getAccount(token: String): ApiCall<UserDto>

    /***
     * Delete an account from the remote data source
     * @param token - The users API token
     * @return ApiCall object with status of the request
     */
    suspend fun deleteAccount(token: String): ApiCall<Unit>

    /***
     * Get all linked accounts for the user
     * @param token - The users API token
     * @return ApiCall object with status of the request and list of links if successful
     */
    suspend fun getLinks(token: String): ApiCall<List<LinkDto>>

    /***
     * Accept a link for the user
     * @param token - The users API token
     * @param id - ID of the account to link with
     * @return ApiCall object with status of the request
     */
    suspend fun acceptLink(id: String, token: String): ApiCall<Unit>

    /***
     * Remove a link from the users account
     * @param token - The users API token
     * @param id - ID of the account to remove link from
     * @return ApiCall object with status of the request
     */
    suspend fun removeLink(id: String, token: String): ApiCall<Unit>

}