package com.celuveat.celeb.application.port.out

interface DeleteInterestedCelebrityPort {
    fun deleteInterestedCelebrity(
        celebrityId: Long,
        memberId: Long,
    )
}
