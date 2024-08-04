package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RestaurantInVideoJpaRepository : JpaRepository<RestaurantInVideoJpaEntity, Long> {
    @Query(
        """
        SELECT new com.celuveat.celeb.adapter.out.persistence.entity.VisitedCelebrity(c, vfr.restaurant.id)
        FROM RestaurantInVideoJpaEntity vfr
        JOIN vfr.video v
        JOIN CelebrityYoutubeContentJpaEntity cy ON cy.youtubeContent.id = v.youtubeContent.id
        JOIN cy.celebrity c
        WHERE vfr.restaurant.id IN :restaurantIds
        """,
    )
    fun findVisitedCelebrities(restaurantIds: List<Long>): List<VisitedCelebrity>
}

data class VisitedCelebrity(
    val celebrity: CelebrityJpaEntity,
    val restaurantId: Long,
)
