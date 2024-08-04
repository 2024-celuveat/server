package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.command.AddInterestedRestaurantCommand

interface AddInterestedRestaurantsUseCase {
    fun addInterestedRestaurant(command: AddInterestedRestaurantCommand)
}
