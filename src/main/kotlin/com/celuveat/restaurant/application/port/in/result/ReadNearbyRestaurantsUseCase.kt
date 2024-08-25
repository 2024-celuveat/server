package com.celuveat.restaurant.application.port.`in`.result

import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery

interface ReadNearbyRestaurantsUseCase {
    fun readNearbyRestaurants(query: ReadNearbyRestaurantsQuery): List<RestaurantPreviewResult>
}
