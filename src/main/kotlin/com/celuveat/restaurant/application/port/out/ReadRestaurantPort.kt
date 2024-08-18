package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.domain.Restaurant

interface ReadRestaurantPort {
    fun readVisitedRestaurantByCelebrity(
        celebrityId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun readById(id: Long): Restaurant

    fun readCelebrityRecommendRestaurant(): List<Restaurant>

    fun readRestaurantsByCondition(
        category: String?,
        region: String?,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun readLatestUpdatedRestaurants(page: Int, size: Int): SliceResult<Restaurant>
}
