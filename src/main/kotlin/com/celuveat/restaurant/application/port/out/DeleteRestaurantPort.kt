package com.celuveat.restaurant.application.port.out

interface DeleteRestaurantPort {
    fun deleteInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    )
}
