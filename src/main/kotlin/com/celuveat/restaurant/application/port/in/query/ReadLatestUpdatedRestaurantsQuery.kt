package com.celuveat.restaurant.application.port.`in`.query

private const val DEFAULT_LATEST_UPDATED_RESTAURANTS_SIZE = 10

data class ReadLatestUpdatedRestaurantsQuery(
    val memberId: Long?,
    val page: Int = 0,
    val size: Int = DEFAULT_LATEST_UPDATED_RESTAURANTS_SIZE,
) {
}
