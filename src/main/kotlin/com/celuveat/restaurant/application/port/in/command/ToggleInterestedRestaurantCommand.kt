package com.celuveat.restaurant.application.port.`in`.command

data class ToggleInterestedRestaurantCommand(
    val memberId: Long,
    val restaurantId: Long,
)
