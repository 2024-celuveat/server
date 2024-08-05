package com.celuveat.celeb.application

import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import com.celuveat.celeb.application.port.out.DeleteCelebrityPort
import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.celeb.application.port.out.SaveCelebrityPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CelebrityService(
    private val findCelebritiesPort: FindCelebritiesPort,
    private val saveCelebrityPort: SaveCelebrityPort,
    private val deleteCelebrityPort: DeleteCelebrityPort,
) : GetInterestedCelebritiesUseCase, AddInterestedCelebrityUseCase, DeleteInterestedCelebrityUseCase {
    override fun getInterestedCelebrities(memberId: Long): List<CelebrityResult> {
        val celebrities = findCelebritiesPort.findInterestedCelebrities(memberId)
        return celebrities.map { CelebrityResult.from(it) }
    }

    @Transactional
    override fun addInterestedCelebrity(command: AddInterestedCelebrityCommand) {
        saveCelebrityPort.saveInterestedCelebrity(command.celebrityId, command.memberId)
    }

    @Transactional
    override fun deleteInterestedCelebrity(command: DeleteInterestedCelebrityCommand) {
        deleteCelebrityPort.deleteInterestedCelebrity(command.celebrityId, command.memberId)
    }
}
