package com.celuveat.restaurant.application.port.`in`.command

data class AddInterestedRestaurantCommand(
    val memberId: Long,
    val restaurantId: Long,
)
