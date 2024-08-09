package com.celuveat.restaurant.application.port.out

interface SaveInterestedRestaurantPort {
    fun saveInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    )
}
