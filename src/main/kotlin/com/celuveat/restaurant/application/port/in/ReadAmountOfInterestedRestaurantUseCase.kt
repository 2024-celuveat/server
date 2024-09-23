package com.celuveat.restaurant.application.port.`in`

interface ReadAmountOfInterestedRestaurantUseCase {
    fun readAmountOfInterestedRestaurant(memberId: Long): Int
}
