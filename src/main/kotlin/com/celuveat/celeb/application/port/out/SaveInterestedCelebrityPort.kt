package com.celuveat.celeb.application.port.out

interface SaveInterestedCelebrityPort {
    fun saveInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    )
}
