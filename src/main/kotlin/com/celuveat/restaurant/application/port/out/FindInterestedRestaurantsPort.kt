package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.ScrollSliceResult
import com.celuveat.restaurant.domain.Restaurant

interface FindInterestedRestaurantsPort {
    fun findInterestedRestaurants(memberId: Long, page: Int, size: Int): ScrollSliceResult<Restaurant>
}
