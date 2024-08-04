package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.command.DeleteInterestedRestaurantCommand

interface DeleteInterestedRestaurantsUseCase {
    fun deleteInterestedRestaurant(command: DeleteInterestedRestaurantCommand)
}
