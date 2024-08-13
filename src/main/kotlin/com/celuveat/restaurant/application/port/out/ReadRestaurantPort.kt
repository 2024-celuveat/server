package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.domain.Restaurant

interface ReadRestaurantPort {
    fun findVisitedRestaurantByCelebrity(
        celebrityId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun getById(id: Long): Restaurant

    fun findCelebrityRecommendRestaurant(): List<Restaurant>
}
