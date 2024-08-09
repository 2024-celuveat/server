package com.celuveat.restaurant.adapter.out.persistence.entity

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InterestedRestaurantJpaRepository : JpaRepository<InterestedRestaurantJpaEntity, Long> {
    @EntityGraph(attributePaths = ["restaurant"])
    fun findAllByMemberId(
        memberId: Long,
        pageable: Pageable,
    ): Slice<InterestedRestaurantJpaEntity>

    @EntityGraph(attributePaths = ["restaurant"])
    fun findByMemberIdAndRestaurantId(
        memberId: Long,
        restaurantId: Long,
    ): InterestedRestaurantJpaEntity?

    fun existsByMemberIdAndRestaurantId(
        memberId: Long,
        restaurantId: Long,
    ): Boolean

    @Query(
        """
        SELECT ir
        FROM InterestedRestaurantJpaEntity ir
        WHERE ir.member.id = :memberId
        AND ir.restaurant.id IN :ids
    """
    )
    fun findAllByMemberIdAndIdIn(
        memberId: Long,
        ids: List<Long>,
    ): List<InterestedRestaurantJpaEntity>
}
