package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.restaurant.adapter.out.persistence.entity.RestaurantJpaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CelebrityRestaurantJpaRepository : JpaRepository<CelebrityRestaurantJpaEntity, Long> {
    @Query(
        """
        SELECT cr.restaurant
        FROM CelebrityRestaurantJpaEntity cr
        WHERE cr.celebrity.id = :celebrityId
        """,
    )
    fun findRestaurantsByCelebrityId(
        celebrityId: Long,
        pageable: Pageable,
    ): Slice<RestaurantJpaEntity>

    @Query(
        """
        SELECT COUNT(cr)
        FROM CelebrityRestaurantJpaEntity cr
        WHERE cr.celebrity.id = :celebrityId
        """,
    )
    fun countRestaurantsByCelebrityId(celebrityId: Long): Long

    @Query(
        """
        SELECT cr.restaurant
        FROM CelebrityRestaurantJpaEntity cr
        GROUP BY cr.restaurant
        ORDER BY COUNT(cr.celebrity) DESC
        LIMIT 10
    """,
    )
    fun findMostVisitedRestaurantsTop10(): List<RestaurantJpaEntity>

    @EntityGraph(attributePaths = ["celebrity"])
    fun findAllByRestaurantIdIn(restaurantIds: List<Long>): List<CelebrityRestaurantJpaEntity>
}
