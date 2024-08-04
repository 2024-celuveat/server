package com.celuveat.restaurant.application.port.`in`.command

data class DeleteInterestedRestaurantCommand(
    val memberId: Long,
    val restaurantId: Long,
)
