package com.celuveat.review.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ReviewImageJpaEntityRepository : JpaRepository<ReviewImageJpaEntity, Long> {
    @Modifying
    @Query("DELETE FROM ReviewImageJpaEntity ri WHERE ri.review.id = :reviewId")
    fun deleteAllByReviewId(reviewId: Long)

    fun findAllByReview(review: ReviewJpaEntity): List<ReviewImageJpaEntity>
}
