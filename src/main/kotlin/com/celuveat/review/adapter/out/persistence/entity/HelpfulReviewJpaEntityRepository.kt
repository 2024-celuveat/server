package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.review.exception.NotFoundHelpfulReviewException
import org.springframework.data.jpa.repository.JpaRepository

interface HelpfulReviewJpaEntityRepository : JpaRepository<HelpfulReviewJpaEntity, Long> {
    fun existsByReviewIdAndClickerId(
        reviewId: Long,
        clickerId: Long,
    ): Boolean

    fun findByClickerIdAndReviewId(
        memberId: Long,
        reviewId: Long,
    ): HelpfulReviewJpaEntity?

    fun getByClickerIdAndReviewId(
        memberId: Long,
        reviewId: Long,
    ): HelpfulReviewJpaEntity {
        return findByClickerIdAndReviewId(memberId, reviewId) ?: throw NotFoundHelpfulReviewException
    }
}
