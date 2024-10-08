package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.domain.InterestedRestaurant

interface ReadInterestedRestaurantPort {
    fun readInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int,
    ): SliceResult<InterestedRestaurant>

    fun existsInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ): Boolean

    fun readInterestedRestaurantsByIds(
        memberId: Long,
        restaurantIds: List<Long>,
    ): List<InterestedRestaurant>

    fun countByMemberId(memberId: Long): Int
}
