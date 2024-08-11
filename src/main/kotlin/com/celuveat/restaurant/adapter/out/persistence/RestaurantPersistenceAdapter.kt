package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.celeb.adapter.out.persistence.entity.CelebrityRestaurantJpaRepository
import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Adapter
class RestaurantPersistenceAdapter(
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
    private val celebrityRestaurantJpaRepository: CelebrityRestaurantJpaRepository,
    private val restaurantJpaRepository: RestaurantJpaRepository
) : ReadRestaurantPort {
    override fun findVisitedRestaurantByCelebrity(
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

    override fun getById(id: Long): Restaurant {
        return restaurantPersistenceMapper.toDomainWithoutImage(restaurantJpaRepository.getById(id))
    }

    companion object {
        val LATEST_SORTER = Sort.by("createdAt").descending()
    }
}
