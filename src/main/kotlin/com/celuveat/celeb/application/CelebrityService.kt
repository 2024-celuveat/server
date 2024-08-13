package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.out.DeleteInterestedCelebrityPort
import com.celuveat.celeb.application.port.out.ReadInterestedCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveInterestedCelebrityPort
import com.celuveat.celeb.exceptions.AlreadyInterestedCelebrityException
import com.celuveat.common.utils.throwWhen
import org.springframework.stereotype.Service

@Service
class CelebrityService(
    private val readInterestedCelebritiesPort: ReadInterestedCelebritiesPort,
    private val saveInterestedCelebrityPort: SaveInterestedCelebrityPort,
    private val deleteInterestedCelebrityPort: DeleteInterestedCelebrityPort,
) : AddInterestedCelebrityUseCase, DeleteInterestedCelebrityUseCase {
    override fun addInterestedCelebrity(command: AddInterestedCelebrityCommand) {
        throwWhen(
            readInterestedCelebritiesPort.existsInterestedCelebrity(command.celebrityId, command.memberId),
        ) { AlreadyInterestedCelebrityException }
        saveInterestedCelebrityPort.saveInterestedCelebrity(command.celebrityId, command.memberId)
    }

    override fun deleteInterestedCelebrity(command: DeleteInterestedCelebrityCommand) {
        deleteInterestedCelebrityPort.deleteInterestedCelebrity(command.celebrityId, command.memberId)
    }
}
