package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadVisitedRestaurantQuery

interface ReadVisitedRestaurantUseCase {
    fun readVisitedRestaurant(command: ReadVisitedRestaurantQuery)
}
