package com.celuveat.restaurant.application.port.`in`

import java.time.LocalDate

interface ReadAmountOfWeeklyUpdateRestaurantsUseCase {
    fun readAmountOfWeeklyUpdateRestaurants(baseDate: LocalDate = LocalDate.now()): Int
}
