package com.example.moodtracker.repository.worry

import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.models.WorryDto
import com.example.moodtracker.utils.mappers.convertToWorryDto
import org.threeten.bp.LocalDateTime

class RemoteMockSource : WorryNetwork {

    private var worries = mutableListOf(
        WorryDto(
            user = "id",
            type = "Current",
            severity = 4,
            description = "Test",
            date = LocalDateTime.of(2020, 4, 1, 0,0),
            solutions = emptyList()
        ),
        WorryDto(
            user = "id",
            type = "Current",
            severity = 4,
            description = "Test",
            date = LocalDateTime.of(2020, 4, 2, 0,0),
            solutions = emptyList()
        )
    )
    private val testToken = "Bearer TestToken"

    override suspend fun addWorry(worry: Worry, token: String): ApiCall<Unit> {
        return if (token == testToken) {
            worries.add(worry.convertToWorryDto())
            ApiCall.success(Unit, 201)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun getWorries(token: String): ApiCall<List<WorryDto>> {
        return if (token == testToken) {
            ApiCall.success(worries, 200)
        } else {
            ApiCall.error(401)
        }
    }

    override suspend fun deleteWorry(token: String, date: LocalDateTime): ApiCall<Unit> {
        return if (token == testToken) {
            ApiCall.success(Unit, 200)
        } else {
            ApiCall.error(401)
        }
    }

}