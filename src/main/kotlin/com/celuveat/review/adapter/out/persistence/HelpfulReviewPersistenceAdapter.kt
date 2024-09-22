package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewJpaRepository
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewPersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaRepository
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.domain.HelpfulReview
import com.celuveat.review.domain.Review
import org.springframework.transaction.annotation.Transactional

// TODO test
@Adapter
class HelpfulReviewPersistenceAdapter(
    private val helpfulReviewJpaRepository: HelpfulReviewJpaRepository,
    private val helpfulReviewPersistenceMapper: HelpfulReviewPersistenceMapper,
    private val reviewImageJpaRepository: ReviewImageJpaRepository,
) : SaveHelpfulReviewPort, ReadHelpfulReviewPort, DeleteHelpfulReviewPort {
    @Transactional
    override fun save(helpfulReview: HelpfulReview): HelpfulReview {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        val saved = helpfulReviewJpaRepository.save(entity)
        val images = reviewImageJpaRepository.findAllByReview(saved.review)
        return helpfulReviewPersistenceMapper.toDomain(saved, images)
    }

    override fun deleteHelpfulReview(helpfulReview: HelpfulReview) {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        helpfulReviewJpaRepository.delete(entity)
    }

    override fun readHelpfulReviewByMemberAndReviews(
        memberId: Long,
        reviews: List<Review>,
    ): List<HelpfulReview> {
        return helpfulReviewJpaRepository.findAllByMemberIdAndReviewIdIn(memberId, reviews.map { it.id })
            .map { helpfulReviewPersistenceMapper.toDomainWithoutImage(it) }
    }

    override fun readByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): HelpfulReview {
        val entity = helpfulReviewJpaRepository.getByMemberIdAndReviewId(
            memberId = memberId,
            reviewId = reviewId,
        )
        val images = reviewImageJpaRepository.findAllByReview(entity.review)
        return helpfulReviewPersistenceMapper.toDomain(entity, images)
    }

    override fun existsByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): Boolean {
        return helpfulReviewJpaRepository.existsByReviewIdAndMemberId(reviewId, memberId)
    }
}
