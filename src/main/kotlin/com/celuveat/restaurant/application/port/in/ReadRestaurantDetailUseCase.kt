package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantDetailResult

interface ReadRestaurantDetailUseCase {
    fun readRestaurantDetail(query: ReadRestaurantQuery): RestaurantDetailResult
}
