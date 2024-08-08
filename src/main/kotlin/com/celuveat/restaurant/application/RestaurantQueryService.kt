package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.GetInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import org.springframework.stereotype.Service

@Service
class RestaurantQueryService(
    private val findRestaurantPort: FindRestaurantPort,
    private val findCelebritiesPort: FindCelebritiesPort,
) : GetInterestedRestaurantsUseCase, ReadVisitedRestaurantUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = findRestaurantPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = findCelebritiesPort.findVisitedCelebritiesByRestaurants(
            interestedRestaurants.contents.map { it.id },
        )
        return interestedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = true,
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readVisitedRestaurant(query: ReadVisitedRestaurantQuery) {
        val visitedRestaurants = findRestaurantPort.findVisitedRestaurantByCelebrity(
            query.celebrityId,
            query.page,
            query.size,
        )
    }
}
