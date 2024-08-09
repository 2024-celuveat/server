package com.celuveat.restaurant.application.port.out

interface DeleteInterestedRestaurantPort {
    fun deleteInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    )
}
