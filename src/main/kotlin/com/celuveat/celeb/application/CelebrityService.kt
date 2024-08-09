package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.DeleteInterestedCelebrityPort
import com.celuveat.celeb.application.port.out.FindInterestedCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveInterestedCelebrityPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CelebrityService(
    private val findInterestedCelebritiesPort: FindInterestedCelebritiesPort,
    private val saveInterestedCelebrityPort: SaveInterestedCelebrityPort,
    private val deleteInterestedCelebrityPort: DeleteInterestedCelebrityPort,
) : GetInterestedCelebritiesUseCase, AddInterestedCelebrityUseCase, DeleteInterestedCelebrityUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = findInterestedCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it.celebrity) }
    }

    @Transactional
    override fun addInterestedCelebrity(command: AddInterestedCelebrityCommand) {
        saveInterestedCelebrityPort.saveInterestedCelebrity(command.celebrityId, command.memberId)
    }

    @Transactional
    override fun deleteInterestedCelebrity(command: DeleteInterestedCelebrityCommand) {
        deleteInterestedCelebrityPort.deleteInterestedCelebrity(command.celebrityId, command.memberId)
    }
}
