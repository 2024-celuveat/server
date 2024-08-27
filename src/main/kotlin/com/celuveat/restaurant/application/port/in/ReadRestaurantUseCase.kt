package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantResult

interface ReadRestaurantUseCase {
    fun readRestaurant(query: ReadRestaurantQuery): RestaurantResult
}
