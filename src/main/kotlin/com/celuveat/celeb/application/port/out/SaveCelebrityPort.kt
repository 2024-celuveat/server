package com.celuveat.celeb.application.port.out

interface SaveCelebrityPort {
    fun saveInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    )
}
