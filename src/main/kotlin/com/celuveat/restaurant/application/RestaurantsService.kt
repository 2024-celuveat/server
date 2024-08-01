package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.FindRestaurantsPort
import org.springframework.stereotype.Service

@Service
class RestaurantsService(
    private val findRestaurantsPort: FindRestaurantsPort,
    private val findCelebritiesPort: FindCelebritiesPort,
) : GetInterestedRestaurantsUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = findRestaurantsPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = findCelebritiesPort.findVisitedCelebritiesByRestaurants(
            interestedRestaurants.contents.map { it.id }
        )
        return interestedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = true,
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }
}
