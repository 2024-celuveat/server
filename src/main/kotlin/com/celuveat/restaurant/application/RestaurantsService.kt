package com.celuveat.restaurant.application

import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.out.FindInterestedRestaurantsPort
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.stereotype.Service

@Service
class RestaurantsService(
    private val findInterestedRestaurantsPort: FindInterestedRestaurantsPort,
) : GetInterestedRestaurantsUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): List<Restaurant> {
        val interestedRestaurants = findInterestedRestaurantsPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        

        return emptyList()
    }
}
