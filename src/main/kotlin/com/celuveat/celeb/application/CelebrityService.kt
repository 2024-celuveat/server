package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.ReadBestCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.out.DeleteInterestedCelebrityPort
import com.celuveat.celeb.application.port.out.FindInterestedCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveInterestedCelebrityPort
import com.celuveat.celeb.exceptions.AlreadyInterestedCelebrityException
import com.celuveat.common.utils.throwWhen
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CelebrityService(
    private val findCelebritiesPort: FindCelebritiesPort,
    private val findInterestedCelebritiesPort: FindInterestedCelebritiesPort,
    private val saveInterestedCelebrityPort: SaveInterestedCelebrityPort,
    private val deleteInterestedCelebrityPort: DeleteInterestedCelebrityPort,
) : GetInterestedCelebritiesUseCase, ReadBestCelebritiesUseCase, AddInterestedCelebrityUseCase, DeleteInterestedCelebrityUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = findCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it) }
    }
    @Transactional
    override fun addInterestedCelebrity(command: AddInterestedCelebrityCommand) {
        throwWhen(
            findInterestedCelebritiesPort.existedInterestedCelebrity(command.celebrityId, command.memberId)
        ) { AlreadyInterestedCelebrityException }
        saveInterestedCelebrityPort.saveInterestedCelebrity(command.celebrityId, command.memberId)
    }

    @Transactional
    override fun deleteInterestedCelebrity(command: DeleteInterestedCelebrityCommand) {
        deleteInterestedCelebrityPort.deleteInterestedCelebrity(command.celebrityId, command.memberId)
    }

    override fun readBestCelebrities(memberId: Long?) {
        TODO()
    }
}
