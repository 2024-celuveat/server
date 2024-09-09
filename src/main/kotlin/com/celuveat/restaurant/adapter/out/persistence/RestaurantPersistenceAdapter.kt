package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.common.utils.geometry.SquarePolygon
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantFilter
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.time.LocalTime

@Adapter
class RestaurantPersistenceAdapter(
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
    private val celebrityRestaurantJpaRepository: CelebrityRestaurantJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository,
) : ReadRestaurantPort {
    override fun readVisitedRestaurantByCelebrity(
        celebrityId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_SORTER)
        val restaurantSlice = celebrityRestaurantJpaRepository.findRestaurantsByCelebrityId(celebrityId, pageRequest)
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurantSlice.content)
            .groupBy { it.restaurant.id }
        return SliceResult.of(
            contents = restaurantSlice.content.map {
                restaurantPersistenceMapper.toDomain(
                    it,
                    imagesByRestaurants[it.id]!!,
                )
            },
            currentPage = page,
            hasNext = restaurantSlice.hasNext(),
        )
    }

    override fun readById(id: Long): Restaurant {
        return restaurantPersistenceMapper.toDomainWithoutImage(restaurantJpaRepository.getById(id))
    }

    override fun readCelebrityRecommendRestaurant(): List<Restaurant> {
        val restaurants = celebrityRestaurantJpaRepository.findMostVisitedRestaurantsTop10()
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurants)
            .groupBy { it.restaurant.id }
        return restaurants.map {
            restaurantPersistenceMapper.toDomain(
                it,
                imagesByRestaurants[it.id]!!,
            )
        }
    }

    override fun readRestaurantsByCondition(
        category: String?,
        region: String?,
        searchArea: SquarePolygon?,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_SORTER)
        val filter = RestaurantFilter(category, region, searchArea)
        val restaurantSlice = restaurantJpaRepository.findAllByFilter(filter, pageRequest)
        val restaurants = restaurantSlice.content.map { it }
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurants)
            .groupBy { it.restaurant.id }
        return SliceResult.of(
            contents = restaurantSlice.content.map {
                restaurantPersistenceMapper.toDomain(
                    it,
                    imagesByRestaurants[it.id] ?: emptyList(),
                )
            },
            currentPage = page,
            hasNext = restaurantSlice.hasNext(),
        )
    }

    override fun readByCreatedAtBetween(
        startOfWeek: LocalDate,
        endOfWeek: LocalDate,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_SORTER)
        val restaurants = restaurantJpaRepository.findByCreatedAtBetween(
            startOfWeek.atStartOfDay(),
            endOfWeek.atTime(LocalTime.MAX),
            pageRequest,
        )
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurants.content)
            .groupBy { it.restaurant.id }
        return SliceResult.of(
            contents = restaurants.content.map {
                restaurantPersistenceMapper.toDomain(
                    it,
                    imagesByRestaurants[it.id]!!,
                )
            },
            currentPage = page,
            hasNext = restaurants.hasNext(),
        )
    }

    override fun readNearby(id: Long): List<Restaurant> {
        val centralRestaurant = restaurantJpaRepository.getById(id)
        val restaurants = restaurantJpaRepository.findTop5NearestInDistance(
            latitude = centralRestaurant.latitude,
            longitude = centralRestaurant.longitude,
        )
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurants)
            .groupBy { it.restaurant.id }
        return restaurants.map {
            restaurantPersistenceMapper.toDomain(
                it,
                imagesByRestaurants[it.id]!!,
            )
        }
    }

    companion object {
        val LATEST_SORTER = Sort.by("createdAt").descending()
    }
}
