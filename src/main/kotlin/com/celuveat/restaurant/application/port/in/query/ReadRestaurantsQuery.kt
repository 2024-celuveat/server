package com.celuveat.restaurant.application.port.`in`.query

import com.celuveat.common.utils.geometry.SquarePolygon

private const val DEFAULT_RESTAURANTS_SIZE = 10

data class ReadRestaurantsQuery(
    val memberId: Long?,
    val category: String?,
    val region: String?,
    val searchArea: SquarePolygon?,
    val page: Int = 0,
    val size: Int = DEFAULT_RESTAURANTS_SIZE,
)

data class CountRestaurantsQuery(
    val category: String?,
    val region: String?,
    val searchArea: SquarePolygon?,
)
