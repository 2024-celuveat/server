package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadPopularRestaurantsUseCase {
    fun readPopularRestaurants(memberId: Long?): List<RestaurantPreviewResult>
}
