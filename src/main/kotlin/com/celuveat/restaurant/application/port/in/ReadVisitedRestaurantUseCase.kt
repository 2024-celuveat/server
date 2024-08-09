package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadVisitedRestaurantUseCase {
    fun readVisitedRestaurant(query: ReadVisitedRestaurantQuery): SliceResult<RestaurantPreviewResult>
}
