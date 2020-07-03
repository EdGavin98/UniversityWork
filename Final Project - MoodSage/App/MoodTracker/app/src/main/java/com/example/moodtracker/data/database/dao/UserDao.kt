package com.example.moodtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.repository.account.AccountLocal

@Dao
interface UserDao : AccountLocal {

    @Insert
    override suspend fun addAccount(user: User)

    @Query("SELECT * FROM users WHERE id = :currentId")
    override suspend fun getAccount(currentId: String): User?

    @Update
    override suspend fun updateAccount(user: User)

    //Delete using query so can just pass id
    @Query("DELETE FROM users WHERE id = :currentId ")
    override suspend fun deleteAccount(currentId: String)


}