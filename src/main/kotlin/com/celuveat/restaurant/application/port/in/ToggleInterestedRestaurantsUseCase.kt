package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.command.ToggleInterestedRestaurantCommand

interface ToggleInterestedRestaurantsUseCase {
    fun toggleInterestedRestaurant(command: ToggleInterestedRestaurantCommand)
}
