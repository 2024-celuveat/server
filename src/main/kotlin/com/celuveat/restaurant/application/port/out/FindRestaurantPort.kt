package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.domain.Restaurant

interface FindRestaurantPort {
    fun findInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun findInterestedRestaurantOrNull(
        memberId: Long,
        restaurantId: Long,
    ): Restaurant?

    fun findVisitedRestaurantByCelebrity(
        celebrityId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>
}
