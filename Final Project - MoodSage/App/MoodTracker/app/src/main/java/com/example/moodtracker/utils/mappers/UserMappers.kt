package com.example.moodtracker.utils.mappers

import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.network.models.UserDto

fun UserDto.convertToUserEntity(): User {
    return User(
        id = id,
        forename = forename,
        surname = surname,
        email = email,
        moodTarget = moodTarget,
        worryTarget = worryTarget
    )
}

fun User.convertToUserDto(): UserDto {
    return UserDto(
        id = id,
        forename = forename,
        surname = surname,
        email = email,
        moodTarget = moodTarget,
        worryTarget = worryTarget
    )
}