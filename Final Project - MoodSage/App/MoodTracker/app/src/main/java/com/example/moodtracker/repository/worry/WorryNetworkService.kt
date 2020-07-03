package com.example.moodtracker.repository.worry

import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.data.network.ApiCall
import com.example.moodtracker.data.network.makeCall
import com.example.moodtracker.data.network.models.WorryDto
import com.example.moodtracker.data.network.retrofit.WorryRetrofitService
import com.example.moodtracker.utils.mappers.convertToWorryDto
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class WorryNetworkService @Inject constructor(
    private val retrofit: WorryRetrofitService
) : WorryNetwork {

    override suspend fun getWorries(token: String): ApiCall<List<WorryDto>> {
        return makeCall {
            retrofit.getWorries(token)
        }
    }

    override suspend fun deleteWorry(token: String, date: LocalDateTime): ApiCall<Unit> {
        return makeCall {
            retrofit.deleteWorry(
                token = token,
                date = date
            )
        }
    }


    override suspend fun addWorry(worry: Worry, token: String): ApiCall<Unit> {
        return makeCall {
            retrofit.addWorry(
                token = token,
                worry = worry.convertToWorryDto()
            )
        }
    }

}