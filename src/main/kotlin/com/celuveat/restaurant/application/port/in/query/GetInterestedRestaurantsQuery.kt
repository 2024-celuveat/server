package com.celuveat.restaurant.application.port.`in`.query

const val DEFAULT_INTERESTED_RESTAURANTS_SIZE = 10

data class GetInterestedRestaurantsQuery(
    val memberId: Long,
    val page: Int,
    val size: Int = DEFAULT_INTERESTED_RESTAURANTS_SIZE,
)
