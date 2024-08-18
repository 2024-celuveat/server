package com.celuveat.restaurant.application.port.`in`.query

import java.time.LocalDate

private const val DEFAULT_LATEST_UPDATED_RESTAURANTS_SIZE = 10

data class ReadWeeklyUpdateRestaurantsQuery(
    val memberId: Long?,
    val page: Int = 0,
    val size: Int = DEFAULT_LATEST_UPDATED_RESTAURANTS_SIZE,
    val baseDate: LocalDate = LocalDate.now(),
)
