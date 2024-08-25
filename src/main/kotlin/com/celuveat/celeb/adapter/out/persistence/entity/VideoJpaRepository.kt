package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VideoJpaRepository : JpaRepository<VideoJpaEntity, Long> {
    @Query(
        """
        SELECT v
        FROM RestaurantInVideoJpaEntity riv
        JOIN riv.video v
        JOIN FETCH v.youtubeContent
        WHERE riv.restaurant.id = :restaurantId
        """
    )
    fun findByRestaurantId(restaurantId: Long): List<VideoJpaEntity>
}
