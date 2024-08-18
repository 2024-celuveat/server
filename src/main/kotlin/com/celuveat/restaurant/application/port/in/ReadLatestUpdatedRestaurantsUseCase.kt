package com.celuveat.restaurant.application.port.`in`

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.query.ReadLatestUpdatedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult

/**
 * 최근 업데이트된 맛집
 */
interface ReadLatestUpdatedRestaurantsUseCase {

    fun readLatestUpdatedRestaurants(query: ReadLatestUpdatedRestaurantsQuery): SliceResult<RestaurantPreviewResult>
}
