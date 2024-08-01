package com.celuveat.restaurant.application.port.`in`

import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.domain.Restaurant

interface GetInterestedRestaurantsUseCase {
    fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): List<Restaurant>
}
