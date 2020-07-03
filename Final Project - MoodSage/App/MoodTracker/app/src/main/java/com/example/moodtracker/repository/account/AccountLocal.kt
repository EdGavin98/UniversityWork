package com.example.moodtracker.repository.account

import com.example.moodtracker.data.database.models.entities.User

interface AccountLocal {

    /***
     * Add a user to the local data source
     * @param user - The user to be added
     * @return Nothing
     */
    suspend fun addAccount(user: User)

    /***
     * Retrieve an account by its id from the local data source
     * @param id - ID of the user
     * @return User if it exists, null if not
     */
    suspend fun getAccount(id: String): User?

    /***
     * Update the user details in the local data source
     * @param user - User details to update with
     * @return Nothing
     */
    suspend fun updateAccount(user: User)

    /***
     *  Remove an account from the local data source
     *  @param id - ID of the account to remove
     *  @return Nothing
     */
    suspend fun deleteAccount(id: String)

}