package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand

interface AddInterestedCelebrityUseCase {
    fun addInterestedCelebrity(command: AddInterestedCelebrityCommand)
}
