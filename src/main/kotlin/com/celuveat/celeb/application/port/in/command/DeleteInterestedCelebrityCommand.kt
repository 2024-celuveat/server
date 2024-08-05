package com.celuveat.celeb.application.port.`in`.command

data class DeleteInterestedCelebrityCommand(
    val memberId: Long,
    val restaurantId: Long,
)
