package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.FindCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.FindInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import org.springframework.stereotype.Service

@Service
class RestaurantQueryServiceCelebrity(
    private val findRestaurantPort: FindRestaurantPort,
    private val findCelebritiesPort: FindCelebritiesPort,
    private val findInterestedRestaurantPort: FindInterestedRestaurantPort,
) : ReadInterestedRestaurantsUseCase, ReadCelebrityVisitedRestaurantUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = findInterestedRestaurantPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = findCelebritiesPort.findVisitedCelebritiesByRestaurants(
            interestedRestaurants.contents.map { it.restaurant.id },
        )
        return interestedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it.restaurant,
                liked = true,
                visitedCelebrities = celebritiesByRestaurants[it.restaurant.id]!!,
            )
        }
    }

    override fun readCelebrityVisitedRestaurant(query: ReadCelebrityVisitedRestaurantQuery): SliceResult<RestaurantPreviewResult> {
        val visitedRestaurants = findRestaurantPort.findVisitedRestaurantByCelebrity(
            query.celebrityId,
            query.page,
            query.size,
        )
        val visitedRestaurantIds = visitedRestaurants.contents.map { it.id }
        val interestedRestaurants = query.memberId?.let {
            findInterestedRestaurantPort.findInterestedRestaurantsByIds(it, visitedRestaurantIds)
        } ?: emptyList()
        return visitedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.any { interested -> interested.restaurant.id == it.id },
            )
        }
    }
}
