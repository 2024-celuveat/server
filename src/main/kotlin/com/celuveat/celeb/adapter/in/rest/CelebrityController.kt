package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.Auth
import com.celuveat.auth.adaptor.`in`.rest.AuthContext
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import com.celuveat.celeb.adapter.`in`.rest.response.SimpleCelebrityResponse
import com.celuveat.celeb.application.port.`in`.AddInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.DeleteInterestedCelebrityUseCase
import com.celuveat.celeb.application.port.`in`.ReadBestCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.ReadInterestedCelebritiesUseCase
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
    private val readInterestedCelebritiesUseCase: ReadInterestedCelebritiesUseCase,
    private val readBestCelebritiesUseCase: ReadBestCelebritiesUseCase,
    private val addInterestedCelebrityUseCase: AddInterestedCelebrityUseCase,
    private val deleteInterestedCelebrityUseCase: DeleteInterestedCelebrityUseCase,
) : CelebrityApi {
    @GetMapping("/interested")
    override fun readInterestedCelebrities(
        @Auth auth: AuthContext,
    ): List<CelebrityResponse> {
        val memberId = auth.memberId()
        val celebritiesResults = readInterestedCelebritiesUseCase.getInterestedCelebrities(memberId)
        return celebritiesResults.map { CelebrityResponse.from(it) }
    }

    @PostMapping("/interested/{celebrityId}")
    override fun addInterestedCelebrity(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
    ) {
        val memberId = auth.memberId()
        val command = AddInterestedCelebrityCommand(memberId, celebrityId)
        addInterestedCelebrityUseCase.addInterestedCelebrity(command)
    }

    @DeleteMapping("/interested/{celebrityId}")
    override fun deleteInterestedCelebrity(
        @Auth auth: AuthContext,
        @PathVariable celebrityId: Long,
    ) {
        val memberId = auth.memberId()
        val command = DeleteInterestedCelebrityCommand(memberId, celebrityId)
        deleteInterestedCelebrityUseCase.deleteInterestedCelebrity(command)
    }

    @GetMapping("/best")
    override fun readBestCelebrities(): List<SimpleCelebrityResponse> {
        return readBestCelebritiesUseCase.readBestCelebrities().map { SimpleCelebrityResponse.from(it) }
    }
}
