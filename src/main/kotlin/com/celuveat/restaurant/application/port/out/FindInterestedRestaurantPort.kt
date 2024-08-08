package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.domain.InterestedRestaurant

interface FindInterestedRestaurantPort {
    fun findInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int,
    ): SliceResult<InterestedRestaurant>

    fun findInterestedRestaurantOrNull(
        memberId: Long,
        restaurantId: Long,
    ): InterestedRestaurant?

    fun findInterestedRestaurantsByIds(
        memberId: Long,
        restaurantIds: List<Long>
    ): List<InterestedRestaurant>
}
