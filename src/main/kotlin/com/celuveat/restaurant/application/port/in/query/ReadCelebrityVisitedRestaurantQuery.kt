package com.celuveat.restaurant.application.port.`in`.query

private const val DEFAULT_VISITED_RESTAURANTS_SIZE = 10

data class ReadCelebrityVisitedRestaurantQuery(
    val memberId: Long?,
    val celebrityId: Long,
    val page: Int = 0,
    val size: Int = DEFAULT_VISITED_RESTAURANTS_SIZE,
)
