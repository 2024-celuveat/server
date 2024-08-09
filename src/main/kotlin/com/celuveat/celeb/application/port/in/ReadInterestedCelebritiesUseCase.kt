package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.result.CelebrityResult

interface ReadInterestedCelebritiesUseCase {
    fun getInterestedCelebrities(memberId: Long): List<CelebrityResult>
}
