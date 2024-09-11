package com.celuveat.restaurant.application.port.`in`.query

import java.time.LocalDate

data class ReadPopularRestaurantQuery(
    val memberId: Long?,
    val baseDate: LocalDate = LocalDate.now(),
)
