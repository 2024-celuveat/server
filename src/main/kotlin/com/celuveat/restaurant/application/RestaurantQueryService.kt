package com.celuveat.restaurant.application

import com.celuveat.celeb.application.port.out.ReadCelebritiesPort
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityRecommendRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadCelebrityVisitedRestaurantUseCase
import com.celuveat.restaurant.application.port.`in`.ReadInterestedRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadNearbyRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadPopularRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantDetailUseCase
import com.celuveat.restaurant.application.port.`in`.ReadRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.ReadWeeklyUpdateRestaurantsUseCase
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityRecommendRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadCelebrityVisitedRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadInterestedRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadNearbyRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadPopularRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.query.ReadWeeklyUpdateRestaurantsQuery
import com.celuveat.restaurant.application.port.`in`.result.RestaurantDetailResult
import com.celuveat.restaurant.application.port.`in`.result.RestaurantPreviewResult
import com.celuveat.restaurant.application.port.out.ReadInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

@Service
class RestaurantQueryService(
    private val readRestaurantPort: ReadRestaurantPort,
    private val readCelebritiesPort: ReadCelebritiesPort,
    private val readInterestedRestaurantPort: ReadInterestedRestaurantPort,
) : ReadInterestedRestaurantsUseCase,
    ReadCelebrityVisitedRestaurantUseCase,
    ReadCelebrityRecommendRestaurantsUseCase,
    ReadRestaurantsUseCase,
    ReadRestaurantDetailUseCase,
    ReadWeeklyUpdateRestaurantsUseCase,
    ReadNearbyRestaurantsUseCase,
    ReadPopularRestaurantsUseCase {
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
                liked = interestedRestaurants.contains(it.id),
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
                liked = interestedRestaurants.contains(it.id),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readRestaurants(query: ReadRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val restaurants = readRestaurantPort.readRestaurantsByCondition(
            category = query.category,
            region = query.region,
            searchArea = query.searchArea,
            page = query.page,
            size = query.size,
        )
        val restaurantIds = restaurants.contents.map { it.id }
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds)
        return restaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it.id),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readWeeklyUpdateRestaurants(query: ReadWeeklyUpdateRestaurantsQuery): SliceResult<RestaurantPreviewResult> {
        val startOfWeek = query.baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = query.baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        val restaurants = readRestaurantPort.readByCreatedAtBetween(startOfWeek, endOfWeek, query.page, query.size)
        val restaurantIds = restaurants.contents.map { it.id }
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds)
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        return restaurants.convertContent {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it.id),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readNearbyRestaurants(query: ReadNearbyRestaurantsQuery): List<RestaurantPreviewResult> {
        val restaurants = readRestaurantPort.readNearby(query.restaurantId)
        val restaurantIds = restaurants.map { it.id }
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurantIds)
        val celebritiesByRestaurants = readCelebritiesPort.readVisitedCelebritiesByRestaurants(restaurantIds)
        return restaurants.map {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it.id),
                visitedCelebrities = celebritiesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readRestaurantDetail(query: ReadRestaurantQuery): RestaurantDetailResult {
        val restaurant = readRestaurantPort.readById(query.restaurantId)
        val celebrities = readCelebritiesPort.readVisitedCelebritiesByRestaurant(query.restaurantId)
        val liked = query.memberId?.let {
            readInterestedRestaurantPort.existsInterestedRestaurant(
                query.memberId,
                query.restaurantId,
            )
        } ?: false
        return RestaurantDetailResult.of(
            restaurant = restaurant,
            liked = liked,
            visitedCelebrities = celebrities,
        )
    }

    override fun readPopularRestaurants(query: ReadPopularRestaurantQuery): List<RestaurantPreviewResult> {
        val startOfDate = query.baseDate.minusWeeks(1)
        val restaurants = readRestaurantPort.readTop10InterestedRestaurantsInDate(
            startOfDate = startOfDate,
            endOfDate = query.baseDate,
        )
        val interestedRestaurants = readInterestedRestaurants(query.memberId, restaurants.map { it.id })
        return restaurants.map {
            RestaurantPreviewResult.of(
                restaurant = it,
                liked = interestedRestaurants.contains(it.id),
            )
        }
    }

    private fun readInterestedRestaurants(
        memberId: Long?,
        restaurantIds: List<Long>,
    ): Set<Long> {
        return memberId?.let {
            readInterestedRestaurantPort.readInterestedRestaurantsByIds(it, restaurantIds)
                .map { interested -> interested.restaurant.id }.toSet()
        } ?: emptySet()
    }
}
