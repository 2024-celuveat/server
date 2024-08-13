package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.result.BestCelebrityResult

interface ReadBestCelebritiesUseCase {
    fun readBestCelebrities(memberId: Long?): List<BestCelebrityResult>
}
