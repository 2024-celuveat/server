package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadCelebrityRecommendRestaurantsUseCase {
    fun readCelebrityRecommendRestaurants(query: ReadCelebrityRecommendRestaurantsQuery): List<RestaurantPreviewResult>
}
