package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.common.utils.throwWhen
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantPersistenceMapper
import com.celuveat.restaurant.application.port.out.DeleteRestaurantPort
import com.celuveat.restaurant.application.port.out.FindRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveRestaurantPort
import com.celuveat.restaurant.domain.Restaurant
import com.celuveat.restaurant.exception.AlreadyInterestedRestaurantException
import com.celuveat.restaurant.exception.NotFoundInterestedRestaurantException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Adapter
class RestaurantPersistenceAdapter(
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val interestedRestaurantJpaRepository: InterestedRestaurantJpaRepository,
    private val restaurantPersistenceMapper: RestaurantPersistenceMapper,
    private val memberJpaRepository: MemberJpaRepository,
) : FindRestaurantPort, SaveRestaurantPort, DeleteRestaurantPort {
    override fun findInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int,
    ): SliceResult<Restaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_ID_SORTER)
        val restaurantSlice = interestedRestaurantJpaRepository.findRestaurantByMemberId(memberId, pageRequest)
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

    override fun findInterestedRestaurantOrNull(
        memberId: Long,
        restaurantId: Long,
    ): Restaurant? {
        val interestedRestaurant = interestedRestaurantJpaRepository.findByMemberIdAndRestaurantId(
            memberId,
            restaurantId,
        )
        return interestedRestaurant?.restaurant?.let {
            restaurantPersistenceMapper.toDomainWithoutImage(it)
        }
    }

    override fun saveInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ) {
        val memberJpaEntity = memberJpaRepository.getById(memberId)
        val restaurantJpaEntity = restaurantJpaRepository.getById(restaurantId)
        validateExistence(memberId, restaurantId)
        interestedRestaurantJpaRepository.save(
            InterestedRestaurantJpaEntity(
                member = memberJpaEntity,
                restaurant = restaurantJpaEntity,
            ),
        )
    }

    private fun validateExistence(
        memberId: Long,
        restaurantId: Long,
    ) = throwWhen(
        interestedRestaurantJpaRepository.existsByMemberIdAndRestaurantId(memberId, restaurantId),
    ) { throw AlreadyInterestedRestaurantException }

    override fun deleteInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ) {
        return interestedRestaurantJpaRepository.findByMemberIdAndRestaurantId(memberId, restaurantId)
            ?.let { interestedRestaurantJpaRepository.delete(it) }
            ?: throw NotFoundInterestedRestaurantException
    }

    companion object {
        val LATEST_ID_SORTER = Sort.by("id").descending()
    }
}
