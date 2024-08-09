package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CelebrityRestaurantJpaRepository : JpaRepository<CelebrityRestaurantJpaEntity, Long> {
    @Query(
        """
        SELECT cr.restaurant
        FROM CelebrityRestaurantJpaEntity cr
        WHERE cr.celebrity.id = :celebrityId
        """
    )
    fun findRestaurantsByCelebrityId(celebrityId: Long, pageable: Pageable): Slice<RestaurantJpaEntity>
}