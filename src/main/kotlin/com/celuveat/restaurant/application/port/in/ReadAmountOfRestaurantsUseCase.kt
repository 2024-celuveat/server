package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.CountRestaurantsQuery

interface ReadAmountOfRestaurantsUseCase {
    fun readAmountOfRestaurants(query: CountRestaurantsQuery): Int
}
