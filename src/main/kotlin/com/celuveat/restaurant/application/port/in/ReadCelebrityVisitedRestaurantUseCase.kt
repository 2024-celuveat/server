package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

interface ReadCelebrityVisitedRestaurantUseCase {
    fun readCelebrityVisitedRestaurant(query: ReadCelebrityVisitedRestaurantQuery): SliceResult<RestaurantPreviewResult>
}
