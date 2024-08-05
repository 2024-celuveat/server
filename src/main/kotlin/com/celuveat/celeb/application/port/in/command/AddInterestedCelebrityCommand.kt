package com.celuveat.celeb.application.port.`in`.command

data class AddInterestedCelebrityCommand(
    val memberId: Long,
    val celebrityId: Long,
)
