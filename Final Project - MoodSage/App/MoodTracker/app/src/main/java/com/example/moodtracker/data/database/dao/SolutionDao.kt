package com.example.moodtracker.data.database.dao

import androidx.room.*
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import com.example.moodtracker.repository.solution.SolutionLocal
import org.threeten.bp.LocalDateTime

@Dao
interface SolutionDao : SolutionLocal {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addSolution(solution: Solution) : Long

    @Query("SELECT * FROM solutions WHERE id = :id")
    override suspend fun getSolutionById(id: Long) : Solution?

    @Transaction
    @Query("SELECT * FROM worries")
    override suspend fun getAllSolutionsForWorries() : List<WorryWithSolutions>

    @Transaction
    @Query("SELECT * FROM worries WHERE date = :date")
    override suspend fun getWorryByDateWithSolutions(date: LocalDateTime): WorryWithSolutions?

    @Update
    override suspend fun updateSolution(solution: Solution)
}