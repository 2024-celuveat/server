package com.celuveat.restaurant.application.port.`in`

interface ReadAmountOfRestaurantByCelebrityUseCase {
    fun readAmountOfRestaurantByCelebrity(celebrityId: Long): Int
}
