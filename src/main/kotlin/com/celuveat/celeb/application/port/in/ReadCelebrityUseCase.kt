package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.query.ReadCelebrityQuery
import com.celuveat.celeb.application.port.`in`.result.CelebrityWithInterestedResult

interface ReadCelebrityUseCase {
    fun readCelebrity(query: ReadCelebrityQuery): CelebrityWithInterestedResult
}
