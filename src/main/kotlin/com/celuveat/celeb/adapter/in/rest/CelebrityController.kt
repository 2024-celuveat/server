package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.ReadBestCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.command.AddInterestedCelebrityCommand
import com.celuveat.celeb.application.port.`in`.command.DeleteInterestedCelebrityCommand
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/celebrities")
@RestController
class CelebrityController(
    private val getInterestedCelebritiesUseCase: GetInterestedCelebritiesUseCase,
    private val readBestCelebritiesUseCase: ReadBestCelebritiesUseCase,
    private val addInterestedCelebrityUseCase: AddInterestedCelebrityUseCase,
    private val deleteInterestedCelebrityUseCase: DeleteInterestedCelebrityUseCase,
) : CelebrityApi {
    @GetMapping("/interested")
    override fun readInterestedCelebrities(
        @AuthId memberId: Long,
    ): List<CelebrityResponse> {
        val celebritiesResults = getInterestedCelebritiesUseCase.getInterestedCelebrities(memberId)
        return celebritiesResults.map { CelebrityResponse.from(it) }
    }

    @PostMapping("/interested/{celebrityId}")
    override fun addInterestedCelebrity(
        @AuthId memberId: Long,
        @PathVariable celebrityId: Long,
    ) {
        val command = AddInterestedCelebrityCommand(memberId, celebrityId)
        addInterestedCelebrityUseCase.addInterestedCelebrity(command)
    }

    @DeleteMapping("/interested/{celebrityId}")
    override fun deleteInterestedCelebrity(
        @AuthId memberId: Long,
        @PathVariable celebrityId: Long,
    ) {
        val command = DeleteInterestedCelebrityCommand(memberId, celebrityId)
        deleteInterestedCelebrityUseCase.deleteInterestedCelebrity(command)
    }

    @GetMapping("/best")
    override fun readBestCelebrities(): List<SimpleCelebrityResponse> {
        return readBestCelebritiesUseCase.readBestCelebrities().map { SimpleCelebrityResponse.from(it) }
    }
}
