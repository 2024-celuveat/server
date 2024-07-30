package com.celuveat.celeb.adapter.`in`.rest

import com.celuveat.auth.adaptor.`in`.rest.AuthId
import com.celuveat.celeb.application.port.`in`.GetInterestedCelebritiesUseCase
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/celebrities")
@RestController
class CelebrityController(
    private val getInterestedCelebritiesUseCase: GetInterestedCelebritiesUseCase,
) {

    @GetMapping("/interested")
    fun getInterestedCelebrities(
        @AuthId memberId: Long,
    ): List<CelebrityResult> {
        return getInterestedCelebritiesUseCase.getInterestedCelebrities(memberId)
    }
}
