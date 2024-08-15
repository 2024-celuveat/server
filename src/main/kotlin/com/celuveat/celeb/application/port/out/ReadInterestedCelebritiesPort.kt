package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.InterestedCelebrity

interface ReadInterestedCelebritiesPort {
    fun readInterestedCelebrities(memberId: Long): List<InterestedCelebrity>

    fun existsInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ): Boolean
}
