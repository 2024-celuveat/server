package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.InterestedCelebrity

interface ReadInterestedCelebritiesPort {
    fun findInterestedCelebrities(memberId: Long): List<InterestedCelebrity>

    fun existsInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    ): Boolean
}
