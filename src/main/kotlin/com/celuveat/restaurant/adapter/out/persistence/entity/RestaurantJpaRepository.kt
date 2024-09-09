package com.celuveat.restaurant.adapter.out.persistence.entity

import com.celuveat.common.utils.findByIdOrThrow
import com.celuveat.restaurant.exception.NotFoundRestaurantException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface RestaurantJpaRepository : JpaRepository<RestaurantJpaEntity, Long>, CustomRestaurantRepository {
    override fun getById(id: Long): RestaurantJpaEntity {
        return findByIdOrThrow(id) { NotFoundRestaurantException }
    }

    @Query("SELECT r FROM RestaurantJpaEntity r WHERE r.createdAt >= :startOfWeek AND r.createdAt <= :endOfWeek ORDER BY r.createdAt DESC")
    fun findByCreatedAtBetween(
        startOfWeek: LocalDateTime,
        endOfWeek: LocalDateTime,
        pageable: Pageable,
    ): Slice<RestaurantJpaEntity>

    @Query(
        """
    SELECT r.*
    FROM restaurant r
    WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.latitude)))) < :distanceKilometer
    ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.latitude))))
    LIMIT 5 OFFSET 1 -- 1 is the itself location
    """,
        nativeQuery = true,
    )
    fun findTop5NearestInDistance(
        latitude: Double,
        longitude: Double,
        distanceKilometer: Double = 2.0,
    ): List<RestaurantJpaEntity>
}
