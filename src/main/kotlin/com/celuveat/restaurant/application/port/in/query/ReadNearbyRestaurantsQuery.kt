package com.celuveat.restaurant.application.port.`in`.query

data class ReadNearbyRestaurantsQuery(
    val memberId: Long?,
    val longitude: Double,
    val latitude: Double,
)
