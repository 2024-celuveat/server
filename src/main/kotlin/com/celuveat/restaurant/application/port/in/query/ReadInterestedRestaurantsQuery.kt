package com.celuveat.restaurant.application.port.`in`.query

private const val DEFAULT_INTERESTED_RESTAURANTS_SIZE = 10

data class ReadInterestedRestaurantsQuery(
    val memberId: Long,
    val page: Int = 0,
    val size: Int = DEFAULT_INTERESTED_RESTAURANTS_SIZE,
)
