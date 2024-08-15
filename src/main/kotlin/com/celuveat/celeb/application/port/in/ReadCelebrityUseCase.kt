package com.celuveat.celeb.application.port.`in`

import com.celuveat.celeb.application.port.`in`.query.ReadCelebrityQuery
import com.celuveat.celeb.application.port.`in`.result.CelebrityResult

interface ReadCelebrityUseCase {
    fun readCelebrity(query: ReadCelebrityQuery): Pair<CelebrityResult, Boolean>
}
