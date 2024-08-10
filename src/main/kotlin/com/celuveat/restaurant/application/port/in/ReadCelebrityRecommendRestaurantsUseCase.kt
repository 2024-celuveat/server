package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadCelebrityRecommendRestaurantsUseCase {
    fun readCelebrityRecommendRestaurants(query: ReadRecommendRestaurantsQuery): List<RestaurantPreviewResult>
}
