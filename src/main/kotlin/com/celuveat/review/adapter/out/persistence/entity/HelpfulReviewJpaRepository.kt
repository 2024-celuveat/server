package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.review.exception.NotFoundHelpfulReviewException
import org.springframework.data.jpa.repository.JpaRepository

interface HelpfulReviewJpaRepository : JpaRepository<HelpfulReviewJpaEntity, Long> {
    fun existsByReviewIdAndMemberId(
        reviewId: Long,
        clickerId: Long,
    ): Boolean

    fun findByMemberIdAndReviewId(
        memberId: Long,
        reviewId: Long,
    ): HelpfulReviewJpaEntity?

    fun getByMemberIdAndReviewId(
        memberId: Long,
        reviewId: Long,
    ): HelpfulReviewJpaEntity {
        return findByMemberIdAndReviewId(memberId, reviewId) ?: throw NotFoundHelpfulReviewException
    }

    fun findAllByMemberIdAndReviewIdIn(
        memberId: Long,
        reviewIds: List<Long>,
    ): List<HelpfulReviewJpaEntity>
}
