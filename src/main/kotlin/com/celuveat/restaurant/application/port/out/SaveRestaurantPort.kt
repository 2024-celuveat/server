package com.celuveat.restaurant.application.port.out

import com.celuveat.restaurant.domain.Restaurant

interface SaveRestaurantPort {
    fun save(restaurant: Restaurant)
}
