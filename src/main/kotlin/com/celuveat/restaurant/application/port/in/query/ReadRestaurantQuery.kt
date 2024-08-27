package com.celuveat.restaurant.application.port.`in`.query

data class ReadRestaurantQuery(
    val memberId: Long?,
    val restaurantId: Long,
)
