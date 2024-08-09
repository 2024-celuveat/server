package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.InterestedCelebrity

interface FindInterestedCelebritiesPort {
    fun findInterestedCelebrities(memberId: Long): List<InterestedCelebrity>

    fun existedInterestedCelebrity(celebrityId: Long, memberId: Long): Boolean
}
