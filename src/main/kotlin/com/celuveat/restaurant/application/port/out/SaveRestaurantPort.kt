package com.celuveat.restaurant.application.port.out

interface SaveRestaurantPort {
    fun saveInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    )
}
