package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.celeb.adapter.`in`.rest.response.CelebrityResponse
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/celebrities")
@RestController
class CelebrityController(
    private val getInterestedCelebritiesUseCase: GetInterestedCelebritiesUseCase,
) : CelebrityApi {
    @GetMapping("/interested")
    override fun getInterestedCelebrities(
        @AuthId memberId: Long,
    ): List<CelebrityResponse> {
        val celebritiesResults = getInterestedCelebritiesUseCase.getInterestedCelebrities(memberId)
        return celebritiesResults.map { CelebrityResponse.from(it) }
    }
}
