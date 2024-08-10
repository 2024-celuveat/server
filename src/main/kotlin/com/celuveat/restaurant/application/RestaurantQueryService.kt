package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.GetInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import org.springframework.stereotype.Service

@Service
class RestaurantQueryService(
    private val readRestaurantPort: ReadRestaurantPort,
    private val readCelebritiesPort: ReadCelebritiesPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
) : ReadInterestedRestaurantsUseCase, ReadCelebrityVisitedRestaurantUseCase, ReadCelebrityRecommendRestaurantsUseCase {
    override fun getInterestedRestaurant(query: GetInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = readInterestedRestaurantPort.findInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = readCelebritiesPort.findVisitedCelebritiesByRestaurants(
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
        val visitedRestaurants = readRestaurantPort.findVisitedRestaurantByCelebrity(
            query.celebrityId,
            query.page,
            query.size,
        )
        val visitedRestaurantIds = visitedRestaurants.contents.map { it.id }
        val interestedRestaurants = readInterestedRestaurants(query.memberId, visitedRestaurantIds)
        return visitedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.any { interested -> interested.restaurant.id == it.id },
            )
        }
    }

    override fun readCelebrityRecommendRestaurants(query: ReadCelebrityRecommendRestaurantsQuery): List<RestaurantPreviewResult> {
        val restaurants = readRestaurantPort.findCelebrityRecommendRestaurant()
        val restaurantIds = restaurants.map { it.id }
        val celebritiesByRestaurants = readCelebritiesPort.findVisitedCelebritiesByRestaurants(restaurantIds)
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        return restaurants.map {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.any { interested -> interested.restaurant.id == it.id },
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    private fun readInterestedRestaurants(
        memberId: Long?,
        restaurantIds: List<Long>
    ) = memberId?.let { readInterestedRestaurantPort.findInterestedRestaurantsByIds(it, restaurantIds) } ?: emptyList()
}
