package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand

interface DeleteInterestedCelebrityUseCase {
    fun deleteInterestedCelebrity(command: DeleteInterestedCelebrityCommand)
}
