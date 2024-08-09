package com.celuveat.restaurant.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.member.adapter.out.persistence.entity.MemberJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaEntity
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.InterestedRestaurantPersistenceMapper
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantImageJpaRepository
import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaRepository
import com.celuveat.restaurant.application.port.out.DeleteInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.FindInterestedRestaurantPort
import com.celuveat.restaurant.application.port.out.SaveInterestedRestaurantPort
import com.celuveat.restaurant.domain.InterestedRestaurant
import com.celuveat.restaurant.exception.NotFoundInterestedRestaurantException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

@Adapter
class InterestedRestaurantPersistenceAdapter(
    private val interestedRestaurantJpaRepository: InterestedRestaurantJpaRepository,
    private val restaurantImageJpaRepository: RestaurantImageJpaRepository,
    private val interestedRestaurantPersistenceMapper: InterestedRestaurantPersistenceMapper,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
) : FindInterestedRestaurantPort, SaveInterestedRestaurantPort, DeleteInterestedRestaurantPort {
    @Transactional(readOnly = true)
    override fun findInterestedRestaurants(
        memberId: Long,
        page: Int,
        size: Int,
    ): SliceResult<InterestedRestaurant> {
        val pageRequest = PageRequest.of(page, size, LATEST_SORTER)
        val interests = interestedRestaurantJpaRepository.findAllByMemberId(memberId, pageRequest)
        val restaurants = interests.content.map { it.restaurant }
        val imagesByRestaurants = restaurantImageJpaRepository.findByRestaurantIn(restaurants)
            .groupBy { it.restaurant.id }
        return SliceResult.of(
            contents = interests.content.map {
                interestedRestaurantPersistenceMapper.toDomain(
                    it,
                    imagesByRestaurants[it.restaurant.id]!!,
                )
            },
            currentPage = page,
            hasNext = interests.hasNext(),
        )
    }

    override fun existsInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ): Boolean {
        return interestedRestaurantJpaRepository.existsByMemberIdAndRestaurantId(memberId, restaurantId)
    }

    override fun saveInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ) {
        val memberJpaEntity = memberJpaRepository.getById(memberId)
        val restaurantJpaEntity = restaurantJpaRepository.getById(restaurantId)
        interestedRestaurantJpaRepository.save(
            InterestedRestaurantJpaEntity(
                member = memberJpaEntity,
                restaurant = restaurantJpaEntity,
            ),
        )
    }

    override fun deleteInterestedRestaurant(
        memberId: Long,
        restaurantId: Long,
    ) {
        return interestedRestaurantJpaRepository.findByMemberIdAndRestaurantId(memberId, restaurantId)
            ?.let { interestedRestaurantJpaRepository.delete(it) }
            ?: throw NotFoundInterestedRestaurantException
    }

    override fun findInterestedRestaurantsByIds(
        memberId: Long,
        restaurantIds: List<Long>,
    ): List<InterestedRestaurant> {
        return interestedRestaurantJpaRepository.findAllByMemberIdAndIdIn(memberId, restaurantIds)
            .map { interestedRestaurantPersistenceMapper.toDomain(it) }
    }

    companion object {
        val LATEST_SORTER = Sort.by("createdAt").descending()
    }
}
