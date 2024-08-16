package com.celuveat.restaurant.application.port.`in`.query

private const val DEFAULT_RESTAURANTS_SIZE = 10

data class ReadRestaurantsQuery(
    val memberId: Long?,
    val category: String?,
    val region: String?,
    val page: Int = 0,
    val size: Int = DEFAULT_RESTAURANTS_SIZE,
)
