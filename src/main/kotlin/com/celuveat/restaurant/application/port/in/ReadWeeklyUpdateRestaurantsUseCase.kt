package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

/**
 * 최근 업데이트된 맛집
 */
interface ReadWeeklyUpdateRestaurantsUseCase {

    fun readWeeklyUpdateRestaurants(query: ReadWeeklyUpdateRestaurantsQuery): SliceResult<RestaurantPreviewResult>
}
