package com.celuveat.celeb.application.port.out

import com.celuveat.celeb.domain.Celebrity

interface FindInterestedCelebritiesPort {
    fun findInterestedCelebrities(memberId: Long): List<Celebrity>
}
