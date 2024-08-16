package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.stereotype.Service

@Service
class RestaurantQueryService(
    private val readRestaurantPort: ReadRestaurantPort,
    private val readCelebritiesPort: ReadCelebritiesPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
) : ReadInterestedRestaurantsUseCase,
    ReadCelebrityVisitedRestaurantUseCase,
    ReadCelebrityRecommendRestaurantsUseCase,
    ReadRestaurantsUseCase {
    override fun readInterestedRestaurant(query: ReadInterestedRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val interestedRestaurants = readInterestedRestaurantPort.readInterestedRestaurants(
            query.memberId,
            query.page,
            query.size,
        )
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(
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
        val visitedRestaurants = readRestaurantPort.readVisitedRestaurantByCelebrity(
            query.celebrityId,
            query.page,
            query.size,
        )
        val visitedRestaurantIds = visitedRestaurants.contents.map { it.id }
        val interestedRestaurants = readInterestedRestaurants(query.memberId, visitedRestaurantIds)
        return visitedRestaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it),
            )
        }
    }

    override fun readCelebrityRecommendRestaurants(query: ReadCelebrityRecommendRestaurantsQuery): List<RestaurantPreviewResult> {
        val restaurants = readRestaurantPort.readCelebrityRecommendRestaurant()
        val restaurantIds = restaurants.map { it.id }
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds)
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        return restaurants.map {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    private fun readInterestedRestaurants(
        memberId: Long?,
        restaurantIds: List<Long>,
    ): Set<Restaurant> {
        return memberId?.let {
            readInterestedRestaurantPort.readInterestedRestaurantsByIds(it, restaurantIds)
                .map { interested -> interested.restaurant }.toSet()
        } ?: emptySet()
    }

    override fun readRestaurants(query: ReadRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val restaurants = readRestaurantPort.readRestaurantsByCondition(
            category = query.category,
            region = query.region,
            page = query.page,
            size = query.size
        )
        val restaurantIds = restaurants.contents.map { it.id }
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds)
        return restaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }
}
