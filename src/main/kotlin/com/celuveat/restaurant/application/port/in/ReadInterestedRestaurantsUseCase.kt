package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadInterestedRestaurantsUseCase {
    fun readInterestedRestaurant(query: ReadInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult>
}
