package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadNearbyRestaurantsUseCase {
    fun readNearbyRestaurants(query: ReadNearbyRestaurantsQuery): List<RestaurantPreviewResult>
}
