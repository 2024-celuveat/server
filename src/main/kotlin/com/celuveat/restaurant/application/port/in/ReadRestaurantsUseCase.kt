package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadRestaurantsUseCase {
    fun readRestaurants(query: ReadRestaurantsQuery): SliceResult<RestaurantPreviewResult>
}
