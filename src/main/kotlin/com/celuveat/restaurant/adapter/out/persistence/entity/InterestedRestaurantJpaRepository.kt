package com.celuveat.restaurant.adapter.out.persistence.entity

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InterestedRestaurantJpaRepository : JpaRepository<InterestedRestaurantJpaEntity, Long> {
    @EntityGraph(attributePaths = ["restaurant"])
    fun findAllByMemberId(memberId: Long, pageable: Pageable): Slice<InterestedRestaurantJpaEntity>

    @Query(
        """
        SELECT ir.restaurant
        FROM InterestedRestaurantJpaEntity ir
        WHERE ir.member.id = :memberId
        """
    )
    @EntityGraph(attributePaths = ["restaurant"])
    fun findRestaurantByMemberId(memberId: Long, pageable: Pageable): Slice<RestaurantJpaEntity>
}
