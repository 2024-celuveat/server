package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.domain.Restaurant
import java.time.LocalDate

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
        searchArea: SquarePolygon?,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun readByCreatedAtBetween(
        startOfWeek: LocalDate,
        endOfWeek: LocalDate,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun readNearby(id: Long): List<Restaurant>

    fun readTop10InterestedRestaurantsInDate(
        startOfDate: LocalDate,
        endOfDate: LocalDate,
    ): List<Restaurant>
}
