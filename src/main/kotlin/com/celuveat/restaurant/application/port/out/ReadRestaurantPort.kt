package com.celuveat.restaurant.application.port.out

import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.adapter.`in`.rest.request.ReadCelebrityVisitedRestaurantSortCondition
import com.celuveat.restaurant.domain.Restaurant
import java.time.LocalDate

interface ReadRestaurantPort {
    fun readVisitedRestaurantByCelebrity(
        celebrityId: Long,
        page: Int,
        size: Int,
        sort: ReadCelebrityVisitedRestaurantSortCondition,
    ): SliceResult<Restaurant>

    fun readById(id: Long): Restaurant

    fun readCelebrityRecommendRestaurant(): List<Restaurant>

    fun readRestaurantsByCondition(
        category: String?,
        region: String?,
        searchArea: SquarePolygon?,
        celebrityId: Long?,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun readRestaurantsByCondition(
        category: String?,
        region: String?,
        searchArea: SquarePolygon?,
        celebrityId: Long?,
    ): List<Restaurant>

    fun countRestaurantsByCondition(
        category: String?,
        region: String?,
        searchArea: SquarePolygon?,
        celebrityId: Long?,
    ): Int

    fun readByCreatedAtBetween(
        startOfWeek: LocalDate,
        endOfWeek: LocalDate,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant>

    fun countByCreatedAtBetween(
        startOfWeek: LocalDate,
        endOfWeek: LocalDate,
    ): Int

    fun readNearby(id: Long): List<Restaurant>

    fun readTop10InterestedRestaurantsInDate(
        startOfDate: LocalDate,
        endOfDate: LocalDate,
    ): List<Restaurant>

    fun readByName(name: String): List<Restaurant>

    fun countRestaurantByCelebrity(celebrityId: Long): Int

    fun readCategoriesByKeyword(keyword: String): List<String>
}
