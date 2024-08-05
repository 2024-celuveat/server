package com.celuveat.celeb.application.port.out

interface DeleteCelebrityPort {
    fun deleteInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    )
}
