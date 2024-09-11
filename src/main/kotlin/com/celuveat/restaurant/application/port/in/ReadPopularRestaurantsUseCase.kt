package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadPopularRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadPopularRestaurantsUseCase {
    fun readPopularRestaurants(query: ReadPopularRestaurantQuery): List<RestaurantPreviewResult>
}
