package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.restaurant.application.port.out.FindInterestedRestaurantsPort
import com.celuveat.restaurant.domain.Restaurant
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Adapter
class RestaurantPersistenceAdapter(
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val interestedRestaurantJpaRepository: InterestedRestaurantJpaRepository,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
) : FindInterestedRestaurantsPort {
    override fun findInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int
    ): SliceResult<Restaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_ID_SORTER)
        val restaurantSlice = interestedRestaurantJpaRepository.findRestaurantByMemberId(memberId, pageRequest)
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurantSlice.content)
            .groupBy { it.restaurant.id }
        return SliceResult.of(
            contents = restaurantSlice.content.map {
                restaurantPersistenceMapper.toDomain(
                    it,
                    imagesByRestaurants[it.id]!!
                )
            },
            currentPage = page,
            hasNext = restaurantSlice.hasNext(),
        )
    }

    companion object {
        val LATEST_ID_SORTER = Sort.by("id").descending()
    }
}
